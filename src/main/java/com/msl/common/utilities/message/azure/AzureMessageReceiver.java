package com.msl.common.utilities.message.azure;

import org.springframework.beans.factory.annotation.Value;

public class AzureMessageReceiver extends AbstractAzureMessageReceiver {

    @Value("${amqp.azure.subscriptions.logs}")
    private String log4jSubscription;

    @Override
    public void receive(String message) {
    }

    @Override
    public String signQueueName() {
        return log4jSubscription;
    }
}
