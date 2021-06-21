package com.xiaowenhou.springboot.consumer.ack.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 拉消息的方式，消费端主动拉取
 */
@RestController
public class ConsumerPull {


    private final RabbitTemplate rabbitTemplate;

    public ConsumerPull(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @GetMapping("/rabbitmq/boot/consumer/ack")
    public String sendMessage() {
        return rabbitTemplate.execute(new ChannelCallback<String>() {

            @Override
            public String doInRabbit(Channel channel) throws Exception {
                GetResponse response = channel.basicGet("boot.queue.consumer.ack", false);
                if (Objects.isNull(response)) {
                    return "已消费完所有的消息";
                }

                String message = new String(response.getBody());
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                if (deliveryTag % 2 == 0) {
                    channel.basicAck(deliveryTag, false);
                    System.out.println(message + "被消费。。。。, deliveryTag is : " + deliveryTag);
                } else {
                    System.out.println(message + "被拒绝消费。。。。, deliveryTag is : " + deliveryTag);
                    channel.basicNack(deliveryTag, false, true);
                }


                return message;
            }
        });
    }
}
