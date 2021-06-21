package com.xiaowenhou.springbootdemo.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {


    @RabbitListener(queues = "queue.springboot")
    public void onMessage(@Payload String message, @Header(name = "Hello") String value) {
        System.out.println("message is : " + message);
        System.out.println("value is : " + value);
    }
}
