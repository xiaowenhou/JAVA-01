package com.xiaowenhou.springboot.cluster;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.directExchange("cluster.exchange").durable(true).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable("cluster.queue").build();
    }

    @Bean
    public Binding binding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("cluster.routingKey").noargs();
    }
}
