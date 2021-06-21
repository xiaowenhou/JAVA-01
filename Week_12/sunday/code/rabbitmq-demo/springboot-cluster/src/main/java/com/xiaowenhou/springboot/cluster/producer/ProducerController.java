package com.xiaowenhou.springboot.cluster.producer;

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

    @GetMapping("/rabbit/cluster")
    public String testCluster() {
        final MessageProperties properties = MessagePropertiesBuilder.newInstance().setHeader("HeaderKey", "HeaderValue").build();
        final Message message = MessageBuilder.withBody("Hello, Cluster".getBytes(StandardCharsets.UTF_8))
                .andProperties(properties)
                .build();
        rabbitTemplate.convertAndSend("cluster.exchange", "cluster.queue", message);

        return "success";
    }
}
