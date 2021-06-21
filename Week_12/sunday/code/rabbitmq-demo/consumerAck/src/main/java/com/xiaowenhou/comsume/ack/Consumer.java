package com.xiaowenhou.comsume.ack;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Consumer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();


        channel.queueDeclare("consumer.ack.queue", false, false, false, null);
        //autoAck 设置为false， 表示取消自动确认， 进行手动确认;设置为true，表示直接进行确认
        //第三个参数表示可以创建默认的消费者，当有消息时会触发该消费者执行
        channel.basicConsume("consumer.ack.queue", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                long deliveryTag = envelope.getDeliveryTag();
                System.out.println(new String(body));

                if (deliveryTag % 2 == 0) {
                    System.out.println(deliveryTag + "号消息，确认!");
                    channel.basicAck(deliveryTag, false);
                } else {
                    System.out.println(deliveryTag + "号消息， 不确认!");
                    channel.basicNack(deliveryTag, false, true);
//                    channel.basicReject(deliveryTag, true);
                }
            }
        });
    }
}
