package com.msl.common.utilities.message.azure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties("amqp.azure")
public class AzureServiceBusConfig {

    private String connectionString;
    private Map<String, String> topics;
    private Map<String, String> subscriptions;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public Map<String, String> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, String> topics) {
        this.topics = topics;
    }

    public Map<String, String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Map<String, String> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
