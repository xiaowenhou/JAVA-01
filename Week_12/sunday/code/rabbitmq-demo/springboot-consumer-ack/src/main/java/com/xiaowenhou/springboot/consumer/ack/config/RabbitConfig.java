package com.xiaowenhou.springboot.consumer.ack.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable("boot.queue.consumer.ack").build();
    }


    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.directExchange("boot.exchange.consumer.ack").durable(false).build();
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("boot.routingkey.consumer.ack").noargs();
    }
}
