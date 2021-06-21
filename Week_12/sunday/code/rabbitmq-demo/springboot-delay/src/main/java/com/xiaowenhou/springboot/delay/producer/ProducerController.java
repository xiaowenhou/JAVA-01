package com.xiaowenhou.springboot.delay.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@RestController
public class ProducerController {


    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping("/rabbit/delay")
    public String testRabbitDelay() {

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int delaySec = random.nextInt(10);

            final MessageProperties properties = MessagePropertiesBuilder.newInstance().setHeader("x-delay", delaySec * 1000).build();
            final Message message = MessageBuilder.withBody(("Hello, Delay Message delay " +delaySec + "second.").getBytes(StandardCharsets.UTF_8))
                    .andProperties(properties).build();
            rabbitTemplate.convertAndSend("delay.exchange", "delay.routingKey", message);
        }

        return "success";
    }
}
