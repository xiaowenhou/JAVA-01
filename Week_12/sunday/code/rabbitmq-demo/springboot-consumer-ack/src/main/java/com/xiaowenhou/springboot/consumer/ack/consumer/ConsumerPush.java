package com.xiaowenhou.springboot.consumer.ack.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 推消息的方式，消费端监听， 有消息来就推送到消费端
 */
@Component
public class ConsumerPush {


    /**
     * NONE模式，则只要收到消息后就立即确认（消息出列，标记已消费），有丢失数据的风险
     * AUTO模式，看情况确认，如果此时消费者抛出异常则消息会返回到队列中
     * MANUAL模式，需要显式的调用当前channel的basicAck方法
     */
    @RabbitListener(queues = "boot.queue.consumer.ack")
    public void onMessage(Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, @Payload String message) throws IOException {

        System.out.println("receive message " + message + ", deliverTag is : " + deliveryTag);
//        channel.basicNack(deliveryTag, false, true);
        if (deliveryTag % 2 == 0) {
            channel.basicAck(deliveryTag, false);
        } else {
            System.out.println(message + " 没有被确认.....");
            channel.basicNack(deliveryTag, false, true);
        }
    }

}
