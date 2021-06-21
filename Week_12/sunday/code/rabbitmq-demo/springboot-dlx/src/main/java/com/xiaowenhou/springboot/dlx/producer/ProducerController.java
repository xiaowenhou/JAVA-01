package com.xiaowenhou.springboot.dlx.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class ProducerController {

    private final RabbitTemplate rabbitTemplate;

    public ProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping("/boot/dlx")
    public String dlxTest() {
        final Message message = MessageBuilder.withBody("Hello, Dlx Message".getBytes(StandardCharsets.UTF_8)).build();

        rabbitTemplate.send("normal.boot.exchange", "normal.boot.routingKey", message);

        return "success";
    }
}
