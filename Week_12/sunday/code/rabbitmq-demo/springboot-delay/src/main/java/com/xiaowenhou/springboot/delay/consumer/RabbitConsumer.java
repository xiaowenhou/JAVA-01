package com.xiaowenhou.springboot.delay.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {

    @RabbitListener(queues = "delay.queue")
    public void onMessage(Message message, Channel channel) {
        System.out.println("now time is : " + System.currentTimeMillis());
        System.out.println(new String(message.getBody()));


    }
}
