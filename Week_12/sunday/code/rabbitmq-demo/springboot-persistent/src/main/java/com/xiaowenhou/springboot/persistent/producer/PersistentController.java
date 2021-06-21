package com.xiaowenhou.springboot.persistent.producer;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * 持久化消息demo，Spring amqp，除非手动指定消息是非持久化的，否则默认都是持久化的消息
 *
 */
@RestController
public class PersistentController {

    private final RabbitTemplate rabbitTemplate;

    public PersistentController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/rabbit/persistent/{message}")
    public String persistentMessage(@PathVariable("message") String content) {
        //持久化的消息
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();

        Message message = MessageBuilder.withBody(content.getBytes(StandardCharsets.UTF_8))
                .andProperties(properties)
                .build();

        rabbitTemplate.send("persistent.exchange", "persistent.routingKey", message);

        return "success";
    }


    @GetMapping("/rabbit/nonPersistent/{message}")
    public String nonPersistent(@PathVariable("message") String content) {
        //非持久化的消息
        MessageProperties properties = MessagePropertiesBuilder.newInstance()
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build();

        Message message = MessageBuilder.withBody(content.getBytes(StandardCharsets.UTF_8))
                .andProperties(properties)
                .build();

        rabbitTemplate.send("persistent.exchange", "persistent.routingKey", message);

        return "success";
    }
}
