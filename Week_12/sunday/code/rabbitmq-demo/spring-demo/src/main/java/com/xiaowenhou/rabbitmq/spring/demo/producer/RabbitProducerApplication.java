package com.xiaowenhou.rabbitmq.spring.demo.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.charset.StandardCharsets;

public class RabbitProducerApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RabbitMqProducerConfig.class);


        final RabbitTemplate template = context.getBean(RabbitTemplate.class);

        MessageProperties properties = MessagePropertiesBuilder.newInstance().
                setContentEncoding("utf-8").
                setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).
                setHeader("myKey", "myValue").
                build();

        for (int i = 0; i < 100; i++) {
            Message message = MessageBuilder.withBody(("你好， 世界..." + i).getBytes(StandardCharsets.UTF_8))
                    .andProperties(properties).build();

            template.send("spring.ex", "routingKey.spring", message);
        }


        context.close();
    }
}
