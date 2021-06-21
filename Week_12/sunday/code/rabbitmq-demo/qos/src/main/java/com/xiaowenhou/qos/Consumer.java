package com.xiaowenhou.qos;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Consumer {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUri("amqp://root:123456@192.168.198.100:5672/%2f");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();


        channel.queueDeclare("qos.queue", false, false, false, null);
        //表示当channel中未消费的消息数量达到10条后， 就不再推送消息到消费端， 等到消费端消费完毕之后， 再进行推动
        //false表示限流只针对这个channel有效
        channel.basicQos(10, false);

        channel.basicConsume("qos.queue", false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("消费端接收到: " + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });


        /*
        channel.close();
        connection.close();*/
    }
}
