package com.xiaowenhou.rabbitmq.simple.hello;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HelloProducer {

    public static void main(String[] args) throws IOException, TimeoutException {
        //设置RabbitMQ的IP，端口，虚拟主机，用户名，密码
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.198.100");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("123456");

        //获取连接
        Connection connection = connectionFactory.newConnection();

        //获取channel
        Channel channel = connection.createChannel();
        //声明交换机，名称是exchange.biz， 类型是直连， 不持久化， 不自动删除， 没有额外属性和配置
        channel.exchangeDeclare("exchange.biz", BuiltinExchangeType.DIRECT, false, true, null);

        //声明队列, 名称是queue.biz, 不持久化， 不排他， 自动删除， 没有额外的属性和配置
        channel.queueDeclare("queue.biz", false, false, true, null);

        //将队列和交换机进行绑定
        channel.queueBind("queue.biz", "exchange.biz", "hello.biz", null);

        channel.basicPublish("exchange.biz", "hello.biz", true, false, null, "Hello, World! 2".getBytes());

        channel.close();
        connection.close();
    }
}
