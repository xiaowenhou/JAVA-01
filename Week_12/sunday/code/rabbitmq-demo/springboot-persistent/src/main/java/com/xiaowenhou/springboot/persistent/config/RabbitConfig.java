package com.xiaowenhou.springboot.persistent.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.directExchange("persistent.exchange").durable(true).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable("persistent.queue").build();
    }

    @Bean
    public Binding binding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("persistent.routingKey").noargs();
    }
}
