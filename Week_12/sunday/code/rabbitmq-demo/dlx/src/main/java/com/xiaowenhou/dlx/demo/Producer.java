package com.xiaowenhou.dlx.demo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Producer {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");

        try (final Connection connection = connectionFactory.newConnection();
             final Channel channel = connection.createChannel();) {

            //申明一个死信队列，和交换机， 并且将队列和交换机进行绑定
            channel.exchangeDeclare("dlx.exchange", BuiltinExchangeType.DIRECT, false, false, null);
            channel.queueDeclare("dlx.queue", false, false, false, null);
            channel.queueBind("dlx.queue", "dlx.exchange", "dlx.routingKey");
            //配置TTL和死信队列相关的参数
            Map<String, Object> argument = new HashMap<>(8);
            argument.put("x-message-ttl", 1000 * 10);
            argument.put("x-dead-letter-exchange", "dlx.exchange");
            argument.put("x-dead-letter-routing-key", "dlx.routingKey");

            //声明正常的队列，交换机以及绑定，带argument参数
            channel.queueDeclare("normal.queue", false, false, false, argument);
            channel.exchangeDeclare("normal.exchange", BuiltinExchangeType.DIRECT, false, false, null);
            channel.queueBind("normal.queue", "normal.exchange", "normal.routingKey");

            //向正常的队列中发送消息
            channel.basicPublish("normal.exchange", "normal.routingKey", null, "Hello, Dlx Message".getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
