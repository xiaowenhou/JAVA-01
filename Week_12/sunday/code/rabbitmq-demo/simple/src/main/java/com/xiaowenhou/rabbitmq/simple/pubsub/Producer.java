package com.xiaowenhou.rabbitmq.simple.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare("fanout.ex", BuiltinExchangeType.FANOUT);

        for (int i = 0; i < 15; i++) {
            channel.basicPublish("fanout.ex", "", null, ("消息" + i).getBytes(StandardCharsets.UTF_8));
        }

        channel.close();
        connection.close();
    }
}




