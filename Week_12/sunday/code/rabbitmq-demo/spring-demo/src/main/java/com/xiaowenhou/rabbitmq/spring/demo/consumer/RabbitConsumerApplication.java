package com.xiaowenhou.rabbitmq.spring.demo.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class RabbitConsumerApplication {

    public static void main(String[] args) throws UnsupportedEncodingException {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(RabbitMqConsumerConfig.class);

        final RabbitTemplate template = context.getBean(RabbitTemplate.class);

        Message message = template.receive("queue.spring.demo");
        if (Objects.isNull(message)) {
            System.out.println("message is null");
            context.close();
            return;
        }

        System.out.println(new String(message.getBody(), message.getMessageProperties().getContentEncoding()));
        context.close();
    }
}
