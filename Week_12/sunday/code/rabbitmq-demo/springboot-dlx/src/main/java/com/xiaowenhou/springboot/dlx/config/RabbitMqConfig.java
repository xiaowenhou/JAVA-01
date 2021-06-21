package com.xiaowenhou.springboot.dlx.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    /**
     * 死信交换机
     */
    @Bean("dlxExchange")
    public Exchange dlxExchange() {
        return ExchangeBuilder.directExchange("dlx.boot.exchange").durable(true).build();
    }

    /**
     * 死信队列
     */
    @Bean("dlxQueue")
    public Queue dlxQueue() {
        return QueueBuilder.durable("dlx.boot.queue").build();
    }

    /**
     * 将死信队列和死信交换机绑定在一起
     */
    @Bean("dlxBind")
    public Binding dlxBinding(@Qualifier("dlxQueue") Queue queue, @Qualifier("dlxExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dlx.boot.routingKey").noargs();
    }


    /**
     * 普通的业务交换机
     */
    @Bean("normalExchange")
    public Exchange normalExchange() {
        return ExchangeBuilder.directExchange("normal.boot.exchange").build();
    }

    /**
     * 普通的业务Queue，TTL-10s
     * 在业务队列上设置死信队列和绑定的Key
     */
    @Bean("queue")
    public Queue queueNormal() {
        return QueueBuilder.durable("normal.boot.queue")
                .ttl(1000 * 10)
                .expires(50 * 1000)
                .deadLetterExchange("dlx.boot.exchange")
                .deadLetterRoutingKey("dlx.boot.routingKey")
                .build();
    }

    /**
     * 将业务的交换机和Queue进行绑定
     */
    @Bean("normalBind")
    public Binding normalBinding(@Qualifier("queue") Queue queue, @Qualifier("normalExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("normal.boot.routingKey").noargs();
    }
}
