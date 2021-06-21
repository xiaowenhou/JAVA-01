package com.xiaowenhou.springboot.delay.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.directExchange("delay.exchange")
                .delayed().build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable("delay.queue").build();
    }


    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("delay.routingKey").noargs();
    }
}
