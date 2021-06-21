package com.xiaowenhou.springbootdemo.producer.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class ProducerController {

    @Autowired
    AmqpTemplate rabbitTemplate;

    @GetMapping("/rabbit/{parameter}")
    public String producer(@PathVariable("parameter") String parameter) {

        Message message = MessageBuilder.withBody(parameter.getBytes(StandardCharsets.UTF_8)).
                setHeader("Hello", "World").build();

        rabbitTemplate.send("exchange.springboot", "routingKey.springboot", message);

        return "success";
    }
}
