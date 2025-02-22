package com.backend.New.Test.RabbitMqConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQ {

    @Value("${rabbitmq.queueName}")
    private String queueName;

    @Value("${rabbitmq.exchangeName}")
    private String exchange;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;

    @Bean
    public Queue queue (){
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }
}
