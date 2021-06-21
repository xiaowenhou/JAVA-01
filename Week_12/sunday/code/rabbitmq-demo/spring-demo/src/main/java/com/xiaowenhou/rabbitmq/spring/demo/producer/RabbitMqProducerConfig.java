package com.xiaowenhou.rabbitmq.spring.demo.producer;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class RabbitMqProducerConfig {

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
 /*   @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);
        return rabbitAdmin;
    }*/


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        return template;
    }

    @Bean
    public Exchange exchange() {
        Exchange exchange = new DirectExchange("spring.ex", false, false, null);
        return exchange;
    }

    @Bean
    public Queue queue() {
        Queue queue = QueueBuilder.nonDurable("queue.spring.demo").build();
        return queue;
    }


    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
      return BindingBuilder.bind(queue).to(exchange).with("routingKey.spring").noargs();
    }
}
