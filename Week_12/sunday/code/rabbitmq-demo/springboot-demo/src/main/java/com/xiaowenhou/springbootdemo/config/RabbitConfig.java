package com.xiaowenhou.springbootdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.directExchange("exchange.springboot").durable(false).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable("queue.springboot").build();
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("routingKey.springboot").noargs();
    }
}
