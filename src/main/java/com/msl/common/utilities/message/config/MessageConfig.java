package com.msl.common.utilities.message.config;

import com.msl.common.utilities.message.MessageSender;
import com.msl.common.utilities.message.azure.AzureMessageShortSender;
import com.msl.common.utilities.message.azure.AzureServiceBusConfig;
import com.msl.common.utilities.message.rabbit.RabbitMessageSender;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MessageConfig {
    @Autowired
    private Environment environment;
    @Autowired(required = false)
    private AmqpTemplate amqpTemplate;
    @Autowired(required = false)
    private AzureServiceBusConfig azureServiceBusConfig;

    @Bean
    public MessageSender messageSender() {
        String amqpName = environment.getProperty("amqp.enable.name");
        if ("azure".equals(amqpName)) {
            AzureMessageShortSender azureMessageSender = new AzureMessageShortSender();
            azureMessageSender.setAzureServiceBusConfig(azureServiceBusConfig);
            return azureMessageSender;
        }
        if ("rabbit".equals(amqpName)) {
            RabbitMessageSender rabbitMessageSender = new RabbitMessageSender();
            rabbitMessageSender.setAmqpTemplate(amqpTemplate);
            return rabbitMessageSender;
        }
        return null;
    }
}
