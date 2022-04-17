package io.hots.mq;

import com.rabbitmq.client.Channel;
import io.hots.constant.RedisKey;
import io.hots.entity.ProductMessage;
import io.hots.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Albin_data@163.com
 * @date 2022/3/15 9:05 下午
 */

@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.stock_release_queue}")
public class ProductStockMQListener {


    @Autowired
    private ProductService productService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;


    /**
     *
     * 重复消费-幂等性
     *
     * @param productMessage
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void releaseStock(ProductMessage productMessage, Message message, Channel channel) throws IOException {

        log.info("监听到释放库存消息：releaseStock：{}", productMessage);
        long msgTag = message.getMessageProperties().getDeliveryTag();
        String repeatFlag = redisTemplate.opsForValue().get(Long.toString(msgTag));
        if (repeatFlag !=null){
            channel.basicAck(msgTag,false);
            return;
        }

        try {
            boolean flag = productService.releaseProductStock(productMessage);
            if (flag) {
                //确认消息消费成功
                channel.basicAck(msgTag, false);
                //消息消费成功标志
                redisTemplate.opsForValue().set(Long.toString(msgTag),"1");
                //redis库存增加
                String key = String.format(RedisKey.SEC_KILL_STOCK_KEY,productMessage.getProductId());
                redisTemplate.opsForValue().increment(key);
            }else {
                channel.basicReject(msgTag,true);
                log.error("释放商品库存失败 flag=false,{}",productMessage);
            }

        } catch (IOException e) {
            log.error("释放商品库存异常:{},msg:{}",e,productMessage);
            channel.basicReject(msgTag,true);
        }

    }



}

