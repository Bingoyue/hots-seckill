package io.hots.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Albin_data@163.com
 * @date 2022/3/14 7:41 下午
 */

@Data
@Configuration
public class RabbitMQConfig {
    /**
     * 交换机
     */
    @Value("${mqconfig.order_event_exchange}")
    private String eventExchange;


    /**
     * 订单队列
     */
    @Value("${mqconfig.order_queue}")
    private String orderQueue;

    /**
     * 进入订单队列的路由key
     */
    @Value("${mqconfig.order_routing_key}")
    private String orderRoutingKey;

    /**
     * 消息转换器
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 创建交换机 Topic类型
     * @return
     */
    @Bean
    public Exchange orderEventExchange(){
        return new TopicExchange(eventExchange,true,false);
    }

    /**
     * 订单监听队列
     */
    @Bean
    public Queue orderQueue(){

        return new Queue(orderQueue,true,false,false);

    }

    /**
     * 绑定关系建立
     * @return
     */
    @Bean
    public Binding orderBinding(){

        return new Binding(orderQueue,Binding.DestinationType.QUEUE,eventExchange,orderRoutingKey,null);
    }

}
