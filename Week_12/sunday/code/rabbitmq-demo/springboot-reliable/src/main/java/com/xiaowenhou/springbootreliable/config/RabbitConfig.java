package com.xiaowenhou.springbootreliable.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {


    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.directExchange("reliable.exchange").autoDelete().build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable("reliable.queue").autoDelete().build();
    }

    @Bean
    public Binding binding(Exchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("reliable.routingKey").noargs();
    }
}
