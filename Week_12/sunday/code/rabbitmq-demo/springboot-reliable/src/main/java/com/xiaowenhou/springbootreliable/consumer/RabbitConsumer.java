package com.xiaowenhou.springbootreliable.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {


    @RabbitListener(queues = "reliable.queue")
    public void onMessage(@Payload String message) {
        System.out.println("receive message is : " + message);
    }
}
