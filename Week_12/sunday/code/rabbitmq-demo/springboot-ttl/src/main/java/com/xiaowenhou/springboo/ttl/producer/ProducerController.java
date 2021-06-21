package com.xiaowenhou.springboo.ttl.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class ProducerController {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping("/ttl/queue")
    public String ttlQueueTest() {
        System.out.println("/ttl/queue execute...");
        final Message message = MessageBuilder.withBody("Hello,NormalMessage".getBytes(StandardCharsets.UTF_8)).build();

        rabbitTemplate.send("ttl.boot.exchange", "ttl.boot.routingKey", message);

        return "success";
    }

    @GetMapping("/ttl/message")
    public String ttlMessageTest() {
        System.out.println("/ttl/message execute...");

        //设置消息的过期时间
        //RabbitMQ只会对队列头部的消息进行过期淘汰， 如果单独给消息设置TTL， 先入队列的消息过期时间设置的比较长或者没有设置过期时间，
        //则后入队的消息会不及时地进行淘汰， 导致消息的堆积
        final MessageProperties properties = MessagePropertiesBuilder.newInstance().setExpiration("10000").build();
        final Message message = MessageBuilder.withBody("Hello, ExpiresMessage".getBytes(StandardCharsets.UTF_8))
                .andProperties(properties).build();

        rabbitTemplate.send("normal.boot.exchange", "normal.boot.routingKey", message);

        return "success";
    }
}
