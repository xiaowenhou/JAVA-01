package com.xiaowenhou.rabbitmq.spring.demo.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class MessageListener {


    /**
     * com.rabbitmq.client.Channel channel对象
     * org.springframework.amqp.core.Message message对象 可以直接操作原生的AMQP消息
     * org.springframework.messaging.Message to use the messaging abstraction counterpart
     * @Payload 注解方法参数，该参数的值就是消息体
     * @Header 注解方法参数，访问指定的消息头字段的值
     * @Headers 该注解的方法参数获取该消息的消息头的所有字段，参数类型对应于map集合。
     * MessageHeaders 参数类型，访问所有消息头字段
     * MessageHeaderAccessor or AmqpMessageHeaderAccessor 访问所有消息头字段
     */
    @RabbitListener(queues = "queue.spring.demo")
    public void onMessage(@Payload String message) {
        System.out.println(Thread.currentThread().getName() + " 收到消息: " + message);
    }


   /* @RabbitListener(queues = "queue.spring.demo")
    public void onMessage(Message message) throws UnsupportedEncodingException {
        System.out.println(new String(message.getBody(), message.getMessageProperties().getContentEncoding()));
    }*/

}
