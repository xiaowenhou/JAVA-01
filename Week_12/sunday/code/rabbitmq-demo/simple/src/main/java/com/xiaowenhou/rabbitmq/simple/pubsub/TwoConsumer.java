package com.xiaowenhou.rabbitmq.simple.pubsub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class TwoConsumer {


    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(() ->{
            try {
                Channel channel = connection.createChannel();
                String queue = channel.queueDeclare().getQueue();
                channel.exchangeDeclare("fanout.ex", BuiltinExchangeType.FANOUT);
                channel.queueBind(queue,"fanout.ex", "");

                channel.basicConsume(queue, (consumerTag, message) -> {
                    System.out.println("TwoConsumer: " + Thread.currentThread().getName() + " 收到了: " + new String(message.getBody()));
                }, consumerTag -> {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
