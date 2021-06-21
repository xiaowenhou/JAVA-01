package com.xiaowenhou.rabbitmq.spring.demo.listener;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

/**
 * @author xiaowenhou
 */
@Configuration
@ComponentScan("com.xiaowenhou.rabbitmq.spring.demo.listener")
@EnableRabbit
public class RabbitListenerConfig {

    @Bean
    public ConnectionFactory connectiodnFactory() {
        return new CachingConnectionFactory(URI.create("amqp://root:123456@192.168.198.100:5672/%2f"));
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        return new RabbitTemplate(factory);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.nonDurable("queue.spring.demo").build();
    }


    @Bean
    public RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
     /*   factory.setConcurrentConsumers(5);
        factory.setMaxConcurrentConsumers(15);
        factory.setBatchSize(10);*/

        return factory;
    }
}
