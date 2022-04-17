package io.hots.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.hots.config.RabbitMQConfig;
import io.hots.constant.SecKillKey;
import io.hots.controller.request.LockProductRequest;
import io.hots.controller.request.ProductItemRequest;
import io.hots.entity.ProductEntity;
import io.hots.entity.ProductMessage;
import io.hots.entity.ProductTaskEntity;
import io.hots.enums.BizCodeEnum;
import io.hots.enums.OrderStateEnum;
import io.hots.enums.StockTaskStateEnum;
import io.hots.exception.BizException;
import io.hots.feign.OrderFeignService;
import io.hots.mapper.ProductMapper;
import io.hots.mapper.ProductTaskMapper;
import io.hots.service.ProductService;
import io.hots.util.ResultData;
import io.hots.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Albin_data@163.com
 * @date 2022/3/12 9:54 下午
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ProductTaskMapper productTaskMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitMQConfig rabbitMQConfig;

    @Autowired
    OrderFeignService orderFeignService;

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    /**
     * 商品分页
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map<String, Object> page(int page, int size) {


        Page<ProductEntity> pageInfo = new Page<>(page,size);

        IPage<ProductEntity> productEntityPage =  productMapper.selectPage(pageInfo,null);

        Map<String,Object> pageMap = new HashMap<>(3);

        pageMap.put("total_record",productEntityPage.getTotal());
        pageMap.put("total_page",productEntityPage.getPages());
        pageMap.put("current_data",productEntityPage.getRecords().stream().map(ProductVO::new).collect(Collectors.toList()));

        return pageMap;
    }


    @Override
    public ResultData lockProductStock(LockProductRequest lockProductRequest) {

        String outTradeNo = lockProductRequest.getOrderOutTradeNo();
        Long productId = lockProductRequest.getProductId();
        Long userId = lockProductRequest.getUserId();

        //锁定商品记录
        int rows = productMapper.lockProductStock(productId,1);
        if(rows != 1){
            String orderKey = String.format(SecKillKey.SEC_KILL_ORDER_KEY, userId, productId);
            redisTemplate.opsForValue().set(orderKey,0);
        }else {
            //插入商品product_task
            ProductTaskEntity productTaskEntity = new ProductTaskEntity(productId, StockTaskStateEnum.LOCK.name(),outTradeNo);

            productTaskMapper.insert(productTaskEntity);
            log.info("商品库存锁定-插入商品product_task成功:{}",productTaskEntity);

            // 发送MQ延迟消息，减少商品库存
            ProductMessage productMessage = new ProductMessage();
            productMessage.setOutTradeNo(outTradeNo);
            productMessage.setTaskId(productTaskEntity.getId());
            productMessage.setProductId(productId.toString());

            rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getStockReleaseDelayRoutingKey(),productMessage);
            log.info("商品库存锁定信息延迟消息发送成功:{}",productMessage);

        }
        return ResultData.buildSuccess();
    }

    @Override
    public boolean releaseProductStock(ProductMessage productMessage) {

        //查询工作单状态
        ProductTaskEntity taskDO = productTaskMapper.selectOne(new QueryWrapper<ProductTaskEntity>().eq("id",productMessage.getTaskId()));
        if(taskDO == null){
            log.warn("工作单不存在，消息体为:{}",productMessage);
        }

        //lock状态才处理
        if(taskDO.getLockState().equalsIgnoreCase(StockTaskStateEnum.LOCK.name())){

            //查询订单状态
            ResultData jsonData = orderFeignService.queryProductOrderState(productMessage.getOutTradeNo());

            if(jsonData.getCode() == 0){

                String state = jsonData.getData().toString();

                if(OrderStateEnum.NEW.name().equalsIgnoreCase(state)){
                    //状态是NEW新建状态，则返回给消息队，列重新投递
                    log.warn("订单状态是NEW,返回给消息队列，重新投递:{}",productMessage);
                    return false;
                }

                //如果是已经支付
                if(OrderStateEnum.PAY.name().equalsIgnoreCase(state)){
                    //如果已经支付，修改task状态为finish
                    taskDO.setLockState(StockTaskStateEnum.FINISH.name());
                    productTaskMapper.update(taskDO,new QueryWrapper<ProductTaskEntity>().eq("id",productMessage.getTaskId()));
                    log.info("订单已经支付，修改库存锁定工作单FINISH状态:{}",productMessage);
                    return true;
                }
            }

            //订单不存在，或者订单被取消，确认消息,修改task状态为CANCEL,恢复优惠券使用记录为NEW
            log.warn("订单不存在，或者订单被取消，确认消息,修改task状态为CANCEL,恢复商品库存,message:{}",productMessage);
            taskDO.setLockState(StockTaskStateEnum.CANCEL.name());
            productTaskMapper.update(taskDO,new QueryWrapper<ProductTaskEntity>().eq("id",productMessage.getTaskId()));


            //恢复商品库存，集锁定库存的值减去当前购买的值
            productMapper.unlockProductStock(taskDO.getProductId(),taskDO.getBuyNum());

            return true;

        } else {
            log.warn("工作单状态不是LOCK,state={},消息体={}",taskDO.getLockState(),productMessage);
            return true;
        }
    }

}
