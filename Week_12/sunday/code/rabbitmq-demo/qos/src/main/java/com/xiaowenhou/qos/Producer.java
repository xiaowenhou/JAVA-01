package com.xiaowenhou.qos;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class Producer {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();


        channel.exchangeDeclare("qos.exchange", BuiltinExchangeType.DIRECT, false, false, null);
        channel.queueDeclare("qos.queue", false, false, false, null);
        channel.queueBind("qos.queue", "qos.exchange", "qos.routingkey");

        int i = 0;
        do {
            TimeUnit.MILLISECONDS.sleep(500);

            channel.basicPublish("qos.exchange", "qos.routingkey", null, ("消息: " + i++).getBytes(StandardCharsets.UTF_8));

        } while (i != 1000000);


        channel.close();
        connection.close();
    }
}
