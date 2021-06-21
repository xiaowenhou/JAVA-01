package com.xiaowenhou.rabbitmq.simple.routingmode;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class FatalLogConsumer {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("routingKey.ex", BuiltinExchangeType.DIRECT, false, false, null);
        channel.queueDeclare("fatal.queue", false, false, false, null);
        channel.queueBind("fatal.queue", "routingKey.ex", LogLevelEnum.FATAL.getLevel());

        channel.basicConsume("fatal.queue", (consumerTag, message) -> {
            System.out.println("FatalLogConsumer 收到: " + new String(message.getBody()));
        }, consumerTag -> {});
    }
}
