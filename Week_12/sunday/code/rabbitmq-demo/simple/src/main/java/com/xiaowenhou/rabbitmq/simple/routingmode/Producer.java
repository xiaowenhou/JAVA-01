package com.xiaowenhou.rabbitmq.simple.routingmode;

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

    private final static LogLevelEnum[] LOG_LEVEL = {LogLevelEnum.WARN, LogLevelEnum.ERROR, LogLevelEnum.FATAL};

    public static void main(String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();
        channel.exchangeDeclare("routingKey.ex", BuiltinExchangeType.DIRECT, false, false, null);

        for (int i = 0; i < 100; i++) {
            String logLevel = LOG_LEVEL[i % LOG_LEVEL.length].getLevel();

            channel.basicPublish("routingKey.ex",
                    logLevel,
                    null,
                    ("这是一条【" + logLevel + "】级别的日志").getBytes(StandardCharsets.UTF_8));
        }


        channel.close();
        connection.close();
    }
}
