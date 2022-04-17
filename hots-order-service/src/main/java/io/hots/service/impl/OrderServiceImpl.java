package io.hots.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.hots.config.RabbitMQConfig;
import io.hots.constant.SecKillKey;
import io.hots.controller.request.LockProductRequest;
import io.hots.entity.*;
import io.hots.enums.BizCodeEnum;
import io.hots.enums.OrderStateEnum;
import io.hots.exception.BizException;
import io.hots.feign.ProductFeignService;
import io.hots.interceptor.LoginInterceptor;
import io.hots.mapper.OrderItemMapper;
import io.hots.mapper.OrderMapper;
import io.hots.service.OrderService;
import io.hots.util.CommonUtil;
import io.hots.util.ResultData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author Albin_data@163.com
 * @date 2022/3/13 3:44 下午
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    RabbitMQConfig rabbitMQConfig;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ThreadPoolExecutor threadPoolExecutor;


    /**
     * 防重提交
     * 获取最新购物项和价格
     * 订单验价
     * 验证价格
     * 锁定商品库存
     * 创建订单对象
     * 创建子订单对象
     * 发送延迟消息-用于自动关单
     *
     * @param createOrderMessage
     * @return
     */
    @Override
    public boolean createOrder(CreateOrderMessage createOrderMessage) {
        long userId = createOrderMessage.getUserId();
        long productId = createOrderMessage.getProductId();

        // 订单号生成
        String orderOutTradeNo = CommonUtil.getStringNumRandom(32);

        CompletableFuture<Void> lockProductStocksFuture = CompletableFuture.runAsync(() -> {
            //锁定库存
            this.lockProductStocks(userId,productId,orderOutTradeNo);
        }, threadPoolExecutor);

        CompletableFuture<OrderEntity> orderEntityFuture = CompletableFuture.supplyAsync(() -> {
            //创建订单
            return this.saveProductOrder(userId,orderOutTradeNo);
        }, threadPoolExecutor);

        CompletableFuture<Void> saveOrderItemsFuture = orderEntityFuture.thenAcceptAsync(orderEntity -> {
            //创建订单项
            this.saveOrderItems(orderOutTradeNo,orderEntity.getId(),productId);
        }, threadPoolExecutor);

        // 多任务执行组合 阻塞直至所有线程结束
        CompletableFuture.allOf(lockProductStocksFuture, orderEntityFuture, saveOrderItemsFuture).join();


        //发送延迟消息，用于自动关单
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOutTradeNo(orderOutTradeNo);
        rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getOrderCloseDelayRoutingKey(),orderMessage);
        return true;
    }

    @Override
    public String queryProductOrderState(String outTradeNo) {
        OrderEntity orderEntity = orderMapper.selectOne(new QueryWrapper<OrderEntity>().eq("out_trade_no",outTradeNo));

        if(orderEntity == null){
            return "";
        }else {
            return orderEntity.getState();
        }
    }

    /**
     * 定时关单
     * @param orderMessage
     * @return
     */
    @Override
    public boolean closeProductOrder(OrderMessage orderMessage) {

        OrderEntity productOrderDO = orderMapper.selectOne(new QueryWrapper<OrderEntity>().eq("out_trade_no",orderMessage.getOutTradeNo()));

        if(productOrderDO == null){
            //订单不存在
            log.warn("直接确认消息，订单不存在:{}",orderMessage);
            return true;
        }

        if(productOrderDO.getState().equalsIgnoreCase(OrderStateEnum.PAY.name())){
            //已经支付
            log.info("直接确认消息,订单已经支付:{}",orderMessage);
            return true;
        }

        String payResult = "";
        //结果为空，则未支付成功，本地取消订单
        if(StringUtils.isBlank(payResult)){
            orderMapper.updateOrderState(productOrderDO.getOutTradeNo(),OrderStateEnum.CANCEL.name(),OrderStateEnum.NEW.name());
            log.info("结果为空，则未支付成功，本地取消订单:{}",orderMessage);
            return true;
        }else {
            //支付成功，主动的把订单状态改成UI就支付，造成该原因的情况可能是支付通道回调有问题
            log.warn("支付成功，主动的把订单状态改成UI就支付，造成该原因的情况可能是支付通道回调有问题:{}",orderMessage);
            orderMapper.updateOrderState(productOrderDO.getOutTradeNo(),OrderStateEnum.PAY.name(),OrderStateEnum.NEW.name());
            return true;
        }

    }

    /**
     * 新增订单项
     * @param orderOutTradeNo
     * @param orderId
     * @param productId
     */
    private void saveOrderItems(String orderOutTradeNo, Long orderId, Long productId) {
        ProductItemEntity itemDO = new ProductItemEntity();
        itemDO.setBuyNum(1);
        itemDO.setProductId(productId);
        itemDO.setOutTradeNo(orderOutTradeNo);
        itemDO.setCreateTime(new Date());
        itemDO.setOrderId(orderId);
        orderItemMapper.insert(itemDO);
    }

    /**
     * 创建订单
     * @param userId
     * @param orderOutTradeNo
     */
    private OrderEntity saveProductOrder(Long userId, String orderOutTradeNo) {

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);

        orderEntity.setOutTradeNo(orderOutTradeNo);
        orderEntity.setCreateTime(new Date());
        orderEntity.setDel(0);

        orderEntity.setState(OrderStateEnum.NEW.name());

        orderMapper.insert(orderEntity);

        return orderEntity;

    }


    /**
     * 真实扣减库存
     * @param userId
     * @param productId
     * @param orderOutTradeNo
     */
    private void lockProductStocks(Long userId, Long productId, String orderOutTradeNo) {

        LockProductRequest lockProductRequest = new LockProductRequest();
        lockProductRequest.setUserId(userId);
        lockProductRequest.setOrderOutTradeNo(orderOutTradeNo);
        lockProductRequest.setProductId(productId);

        ResultData jsonData = productFeignService.lockProductStock(lockProductRequest);
        if(jsonData.getCode()!=0){
            log.error("锁定商品库存失败：{}",lockProductRequest);
            throw new BizException(BizCodeEnum.ORDER_CONFIRM_LOCK_PRODUCT_FAIL);
        }
    }

    /**
     * 查询用户是否秒杀到
     * @param userId
     * @param productId
     * @return 订单id
     */
    public String getSecKillResult(Long userId, long productId) {


        return "";
    }
}
