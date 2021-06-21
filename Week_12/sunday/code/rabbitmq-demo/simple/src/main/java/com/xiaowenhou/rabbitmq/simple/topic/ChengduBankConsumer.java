package com.xiaowenhou.rabbitmq.simple.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xiaowenhou.rabbitmq.simple.routingmode.LogLevelEnum;

public class ChengduBankConsumer {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("topic.ex", BuiltinExchangeType.TOPIC, true, false, null);
        channel.queueDeclare("chengduBank.queue", false, false, false, null);
        channel.queueBind("chengduBank.queue", "topic.ex", "*.chengdu.bank");

        channel.basicConsume("chengduBank.queue", (consumerTag, message) -> {
            System.out.println("ChengduBankConsumer 收到: " + new String(message.getBody()));
        }, consumerTag -> {});
    }
}
