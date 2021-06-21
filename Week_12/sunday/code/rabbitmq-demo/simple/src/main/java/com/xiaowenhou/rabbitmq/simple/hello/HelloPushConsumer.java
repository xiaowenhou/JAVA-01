package com.xiaowenhou.rabbitmq.simple.hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class HelloPushConsumer {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("queue.biz", false, false, true, null);
        channel.basicConsume("queue.biz", (consumerTag, message) ->{
            System.out.println(consumerTag);
            System.out.println(new String(message.getBody()));
        }, (String consumerTag) -> {

        });

       /* channel.close();
        connection.close();*/
    }
}
