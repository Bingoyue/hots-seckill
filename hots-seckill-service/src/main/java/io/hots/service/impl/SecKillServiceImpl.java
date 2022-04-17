package io.hots.service.impl;

import com.google.common.hash.Hashing;
import io.hots.config.RabbitMQConfig;
import io.hots.constant.RedisKey;
import io.hots.constant.SecKillKey;
import io.hots.controller.request.SecKillItemRequest;
import io.hots.controller.request.SecKillStockRequest;
import io.hots.controller.request.SecKillTimeRequest;
import io.hots.entity.CreateOrderMessage;
import io.hots.entity.LoginUser;
import io.hots.enums.BizCodeEnum;
import io.hots.interceptor.LoginInterceptor;
import io.hots.service.SecKillService;
import io.hots.util.CommonUtil;
import io.hots.util.ResultData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Albin_data@163.com
 * @date 2022/4/15 7:57 下午
 */

@Service
public class SecKillServiceImpl implements SecKillService {

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RabbitMQConfig rabbitMQConfig;

    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    @Override
    public ResultData getSecKillAddress(String product_id) {

        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        if (loginUser == null){
            return ResultData.buildResult(BizCodeEnum.USER_NOT_LOGIN);
        }

        String token =  String.format(SecKillKey.SEC_KILL_ADDRESS_TOKEN, loginUser.getId(), product_id,
                CommonUtil.getStringNumRandom(32));

        String path = createSecKillPath(token);

        redisTemplate.opsForValue().set(path,"1",5, TimeUnit.MINUTES);

        return ResultData.buildSuccess(path);
    }

    /**
     * 秒杀方法
     * 是否登录
     * 是否在秒杀活动时间内
     * 秒杀地址是否正确
     * 该用户该商品是否已经存在订单
     * 查看是否有库存、库存扣减
     * 发送订单消息
     * 添加该用户订单存在标记
     * @return
     */
    @Override
    public ResultData doSecKill(SecKillItemRequest request, String path) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        // 是否登录
        if (loginUser == null){
            return ResultData.buildResult(BizCodeEnum.USER_NOT_LOGIN);
        }

        // 是否在秒杀活动时间内
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Map<String, String> map = (Map<String, String>) redisTemplate.opsForValue().get(String.format(RedisKey.SEC_KILL_TIME_KEY,request.getProductId().toString()));
        if (map !=null){
            LocalDateTime startTime = LocalDateTime.parse(map.get("start_time"), dateTimeFormatter);
            LocalDateTime endTime = LocalDateTime.parse(map.get("end_time"), dateTimeFormatter);
            if (startTime.compareTo(now)>0 || endTime.compareTo(now)<0){
                return ResultData.buildResult(BizCodeEnum.SEC_KILL_TIME_ERROR);
            }
        }else {
            return ResultData.buildResult(BizCodeEnum.SEC_KILL_TIME_ERROR);
        }

        // 秒杀地址是否正确
        Object pathFlag = redisTemplate.opsForValue().get(path);
        if(pathFlag == null){
            return ResultData.buildResult(BizCodeEnum.SEC_KILL_ADDRESS_ERROR);
        }

        // 该用户该商品是否已经存在订单
        String orderKey = String.format(SecKillKey.SEC_KILL_ORDER_KEY, loginUser.getId(), request.getProductId());
        Object orderFlag = redisTemplate.opsForValue().get(orderKey);
        if(orderFlag != null){
            return ResultData.buildResult(BizCodeEnum.SEC_KILL_ORDER_REPEAT);
        }

        //查看是否有库存、库存扣减
        Long stock = redisTemplate.opsForValue().decrement(String.format(RedisKey.SEC_KILL_STOCK_KEY,request.getProductId().toString()));
        if(stock ==null || stock<0){
            return ResultData.buildResult(BizCodeEnum.SEC_KILL_STOCK_SHORTAGE);
        }

        //发送订单消息
        CreateOrderMessage orderMessage = new CreateOrderMessage(loginUser.getId(),request.getProductId());
        rabbitTemplate.convertAndSend(rabbitMQConfig.getEventExchange(),rabbitMQConfig.getOrderRoutingKey(),orderMessage);

        //添加该用户订单存在标记
        redisTemplate.opsForValue().set(orderKey,1);

        return ResultData.buildSuccess();

    }

    @Override
    public void setSecKillTime(SecKillTimeRequest request) {
        String productId = request.getProductId().toString();
        Map<String, String> map = new HashMap<>();
        map.put("start_time", request.getStartTime());
        map.put("end_time", request.getEndTime());
        redisTemplate.opsForValue().set(String.format(RedisKey.SEC_KILL_TIME_KEY, productId),map);
    }

    @Override
    public void setStock(SecKillStockRequest request) {
        redisTemplate.opsForValue().set(String.format(RedisKey.SEC_KILL_STOCK_KEY,request.getProductId().toString()),request.getNum());
    }

    @Override
    public String getSecRepeatToken() {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();

        String token = CommonUtil.getStringNumRandom(32);
        String key = String.format(RedisKey.SUBMIT_ORDER_TOKEN_KEY,loginUser.getId(),token);

        redisTemplate.opsForValue().set(key,"1",5000, TimeUnit.MICROSECONDS);
        return token;
    }

    public String createSecKillPath(String originUrl){
        long murmur32 = Hashing.murmur3_32().hashUnencodedChars(originUrl).padToLong();
        String path = encodeLongToBase62(murmur32);
        return path;
    }

    /**
     * 10进制转62进制
     * @param num
     * @return
     */
    private String encodeLongToBase62(long value){
        StringBuffer sb = new StringBuffer();
        do{
            int num = (int)(value%62);
            sb.append(CHARS.charAt(num));
            value = value/62;
        }while (value>0);

        return sb.reverse().toString();
    }
}
