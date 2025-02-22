package com.backend.New.Test.service;

import com.backend.New.Test.utils.CommonUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.queueName}"})
    public void consumeFileMessage(String message){
        log.info("The message is received by the RabbitMQ Consumer: {}", message);
    }
}
