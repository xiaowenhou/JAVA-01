package com.xiaowenhou.rabbitmq.spring.demo.listener;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RabbitListenerApplication {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(RabbitListenerConfig.class);
    }
}
