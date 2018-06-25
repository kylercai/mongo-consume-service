package com.msl.common.utilities.message.rabbit;

import com.msl.common.utilities.message.MessageSender;
import org.springframework.amqp.core.AmqpTemplate;

import java.util.Date;

public class RabbitMessageSender implements MessageSender {

    private AmqpTemplate amqpTemplate;

    public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public void send(String name, String message) {
        amqpTemplate.convertAndSend(name, message);
    }

    @Override
    public void send(String name, String message, Date sendDate) {
        amqpTemplate.convertAndSend(name, (Object) message, new RabbitExpirationProcessor(sendDate.getTime()));
    }
}
