package com.xiaowenhou.rabbitmq.simple.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class HelloPullConsumer {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException, IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //amqp协议， 用户名root， 密码123456， host10.192.168.100， 端口5672， 虚拟主机为/, %2f是uri中/的转义字符
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();

        GetResponse response = channel.basicGet("queue.biz", true);
        System.out.println(new String(response.getBody()));

        channel.close();
        connection.close();
    }
}
