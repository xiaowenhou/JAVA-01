package com.xiaowenhou.springboot.consumer.ack.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class Producer implements ApplicationRunner {

    private final RabbitTemplate rabbitTemplate;

    public Producer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread.sleep(5000);

        for (int i = 0; i < 10; i++) {
            MessageProperties properties = MessagePropertiesBuilder.newInstance()
                    .setDeliveryTag((long) i).build();
            Message message = MessageBuilder.withBody(("消息" + i).getBytes(StandardCharsets.UTF_8))
                    .andProperties(properties).build();
            rabbitTemplate.convertAndSend("boot.exchange.consumer.ack", "boot.routingkey.consumer.ack", message);
        }
    }
}
