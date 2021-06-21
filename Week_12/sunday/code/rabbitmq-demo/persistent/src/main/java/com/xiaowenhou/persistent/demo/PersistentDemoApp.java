package com.xiaowenhou.persistent.demo;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class PersistentDemoApp {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("persistent.demo.exchange", BuiltinExchangeType.DIRECT, true, false, null);
        channel.queueDeclare("persistent.demo.queue",true, false, false, null);
        channel.queueBind("persistent.demo.queue", "persistent.demo.exchange", "persistent.demo.routingKey");

        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.deliveryMode(2);
        channel.basicPublish("persistent.demo.exchange", "persistent.demo.routingKey", builder.build(), "hello, wolrd".getBytes(StandardCharsets.UTF_8));

        channel.close();
        connection.close();
    }
}
