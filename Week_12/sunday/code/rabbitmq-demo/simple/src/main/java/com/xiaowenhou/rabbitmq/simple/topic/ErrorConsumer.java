package com.xiaowenhou.rabbitmq.simple.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.xiaowenhou.rabbitmq.simple.routingmode.LogLevelEnum;

public class ErrorConsumer {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("topic.ex", BuiltinExchangeType.TOPIC, true, false, null);
        channel.queueDeclare("error.log.queue", false, false, false, null);
        channel.queueBind("error.log.queue", "topic.ex", "error.#");

        channel.basicConsume("error.log.queue", (consumerTag, message) -> {
            System.out.println("ErrorConsumer 收到: " + new String(message.getBody()));
        }, consumerTag -> {});
    }
}
