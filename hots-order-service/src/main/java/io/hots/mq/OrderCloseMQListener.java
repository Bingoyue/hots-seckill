package io.hots.mq;

import com.rabbitmq.client.Channel;
import io.hots.entity.OrderMessage;
import io.hots.service.OrderService;
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
 * @date 2022/3/15 9:31 下午
 */
@Slf4j
@Component
@RabbitListener(queues = "${mqconfig.order_close_queue}")
public class OrderCloseMQListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;


    /**
     * 关单
     * @param orderMessage
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitHandler
    public void closeOrder(OrderMessage orderMessage, Message message, Channel channel) throws IOException {
        log.info("监听到关闭订单消息：closeOrder:{}",orderMessage);
        long msgTag = message.getMessageProperties().getDeliveryTag();
        String repeatFlag = redisTemplate.opsForValue().get(Long.toString(msgTag));
        if (repeatFlag !=null){
            channel.basicAck(msgTag,false);
            return;
        }

        try{

            boolean flag = orderService.closeProductOrder(orderMessage);
            if(flag){
                channel.basicAck(msgTag,false);
                redisTemplate.opsForValue().set(Long.toString(msgTag),"1");
            }else {
                channel.basicReject(msgTag,true);
                log.error("关闭订单失败 flag=false,closeOrder：{}",orderMessage);
            }

        }catch (IOException e){
            log.error("定时关闭订单异常: {}",orderMessage);
            channel.basicReject(msgTag,true);
        }

    }

}

