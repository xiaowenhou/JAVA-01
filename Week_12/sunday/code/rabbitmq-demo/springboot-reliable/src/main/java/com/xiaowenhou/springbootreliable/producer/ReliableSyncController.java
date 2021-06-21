package com.xiaowenhou.springbootreliable.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class ReliableSyncController {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/rabbit/reliable/{message}")
    public String testReliable(@PathVariable("message") String content) {
        Message message = MessageBuilder.withBody(content.getBytes(StandardCharsets.UTF_8)).build();


        try {
            //使用invoke方式， template会单独为这些操作创建一个线程绑定的channel， 并且在操作完毕之后关闭该channel
            Boolean result = rabbitTemplate.invoke(operations -> {

                rabbitTemplate.send("reliable.exchange", "reliable.routingKey", message);

                //等待消息被同步到broker， 可以改写成批量等待
                return rabbitTemplate.waitForConfirms(3000);
            });


            if (!Objects.isNull(result) && result) {
                System.out.println("消息被确认");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("消息没有被确认...");
        }

        return "success";
    }
}


