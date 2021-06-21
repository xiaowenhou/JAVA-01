package com.xiaowenhou.springbootreliable.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Producer端进行消息可靠性的确认，消息投递到rabbit之后，调用异步回调方法，
 * 在方法中可以获取到消息的具体编号，确认消息是否到达了Rabbit
 */
@RestController
public class ReliableAsyncController {

    private final RabbitTemplate rabbitTemplate;


    @Autowired
    public ReliableAsyncController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) ->{
            if (ack) {
                assert correlationData != null;
                String messageId = correlationData.getId();
                System.out.println("消息" + messageId + "得到确认...");

                ReturnedMessage returnedMessage = correlationData.getReturned();
                if (!Objects.isNull(returnedMessage)) {
                    System.out.println("确认内容为: " + new String(returnedMessage.getMessage().getBody()));
                }

            } else {
                //在执行过程中发生错误， 打印cause
                System.out.println(cause);
            }
        });
    }

    @GetMapping("/rabbit/reliable/async/{message}")
    public String testReliable(@PathVariable("message") String content) throws InterruptedException {
        Message message = MessageBuilder.withBody(content.getBytes(StandardCharsets.UTF_8)).build();


        for (int i = 0; i < 1000; i++) {
            CorrelationData data = new CorrelationData();
            data.setId(i+"");
            Message message1 = MessageBuilder.withBody(("消息" + i + "的确认").getBytes(StandardCharsets.UTF_8)).build();
            ReturnedMessage returnedMessage = new ReturnedMessage(message1, 200, null, null, null);
            data.setReturned(returnedMessage);


            rabbitTemplate.send("reliable.exchange", "reliable.routingKey", message, data);
        }

        return "success";
    }
}


