package com.xiaowenhou.springboo.ttl.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean("ttlExchange")
    public Exchange ttlExchange() {
        return ExchangeBuilder.directExchange("ttl.boot.exchange").durable(true).build();
    }
    /**
     * 在队列上设置TTL信息
     */
    @Bean("ttlQueue")
    public Queue queueTtl() {
        return QueueBuilder.durable("ttl.boot.queue").ttl(20 * 1000).expires(40 * 1000).build();
    }

    @Bean("ttlBind")
    public Binding ttlBinding(@Qualifier("ttlQueue") Queue queue, @Qualifier("ttlExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ttl.boot.routingKey").noargs();
    }







    @Bean("normalExchange")
    public Exchange normalExchange() {
        return ExchangeBuilder.directExchange("normal.boot.exchange").build();
    }
    /**
     * 声明普通的Queue
     */
    @Bean("queue")
    public Queue queueNormal() {
        return QueueBuilder.durable("normal.boot.queue").build();
    }
    @Bean("normalBind")
    public Binding normalBinding(@Qualifier("queue") Queue queue, @Qualifier("normalExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("normal.boot.routingKey").noargs();
    }
}
