package com.xiaowenhou.rabbitmq.simple.workqueue;

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

public class Consumer {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");

        Connection connection = connectionFactory.newConnection();

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(() ->{
            try {
                Channel channel = connection.createChannel();
                channel.queueDeclare("queue.workqueue", true, false, false, null);
                channel.basicConsume("queue.workqueue",  (consumerTag, message)->{
                    System.out.println(Thread.currentThread().getName() + " 收到消息: " + new String(message.getBody()));
                }, (consumerTag) ->{});
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }



}
