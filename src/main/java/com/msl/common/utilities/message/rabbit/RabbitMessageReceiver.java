package com.msl.common.utilities.message.rabbit;

import org.springframework.beans.factory.annotation.Value;

public class RabbitMessageReceiver extends AbstractRabbitMessageReceiver {

    @Value("${amqp.rabbit.queues.log4j}")
    private String log4jQueue;

    @Override
    public void receive(String message) {
    }

    @Override
    public String signQueueName() {
        return log4jQueue;
    }
}
