package com.xiaowenhou.rabbitmq.spring.demo.consumer;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class RabbitMqConsumerConfig {

    /**
     * 创建连接工厂
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {

        ConnectionFactory factory = new CachingConnectionFactory(URI.create("amqp://root:123456@192.168.198.100:5672/%2f"));
//        ConnectionFactory factory = new LocalizedQueueConnectionFactory();
//        ConnectionFactory factory = new SimpleRoutingConnectionFactory();
        return factory;
    }


    /**
     * 创建rabbitadmin
     * @param factory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);
        return rabbitAdmin;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        return template;
    }

    @Bean
    public Queue queue() {
        Queue queue = QueueBuilder.nonDurable("queue.spring.demo").build();
        return queue;
    }
}
