package io.hots;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Albin_data@163.com
 * @date 2022/3/15 7:58 下午
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
public class MQTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testSendDelayMsg(){
        rabbitTemplate.convertAndSend("order.event.exchange","order.close.delay.routing.key","test Mq DelayMsgSend");
    }
}
