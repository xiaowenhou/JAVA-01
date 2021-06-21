package com.xiaowenhou.rabbitmq.simple.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Producer {

    private static final String[] LOG_LEVEL = {"info", "warn", "error"};
    private static final String[] LOG_AREA = {"beijing", "shanghai", "chengdu"};
    private static final String[] LOG_BIZ = {"bank", "common", "lfs"};

    private static Random random = new Random();


    public static void main(String[] args) throws  Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();


        //声明交换器
        channel.exchangeDeclare("topic.ex", BuiltinExchangeType.TOPIC, true, false, null);

        for (int i = 0; i < 150; i++) {
            String level = LOG_LEVEL[random.nextInt(LOG_LEVEL.length)];
            String area = LOG_AREA[random.nextInt(LOG_AREA.length)];
            String biz = LOG_BIZ[random.nextInt(LOG_BIZ.length)];

            //level.area.biz
            String routingKey = level + "." + area + "." + biz;
            String message = "日志:[" + level + "]" + "[" + area + "]" + "[" + biz + "]";
            channel.basicPublish("topic.ex", routingKey, null, message.getBytes(StandardCharsets.UTF_8));
        }


        channel.close();
        connection.close();
    }
}
