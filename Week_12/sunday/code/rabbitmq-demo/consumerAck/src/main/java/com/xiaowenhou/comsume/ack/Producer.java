package com.xiaowenhou.comsume.ack;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class Producer {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("consumer.ack.exchange", BuiltinExchangeType.DIRECT, false, false, null);
        channel.queueDeclare("consumer.ack.queue", false, false, false, null);
        channel.queueBind("consumer.ack.queue", "consumer.ack.exchange", "consumer.ack.routingkey");

        for (int i = 0; i < 10; i++) {
            channel.basicPublish("consumer.ack.exchange", "consumer.ack.routingkey", null, ("Hello, Message " + i).getBytes(StandardCharsets.UTF_8));
        }

        channel.close();
        connection.close();
    }
}
