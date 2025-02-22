package com.backend.New.Test.service;

import com.backend.New.Test.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

    @Value("${rabbitmq.exchangeName}")
    private String exchange;

    @Value("${rabbitmq.routingKey}")
    private String routineKey;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendFileMessage(String message){
        CommonUtils.logInfo("The file  message is sent: "+message);
        rabbitTemplate.convertAndSend(exchange,routineKey,message);
    }
}
