package com.msl.common.utilities.message.azure;

import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import com.msl.common.utilities.message.MessageSender;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Date;

/**
 * Azure消息发送（短连接）
 */
public class AzureMessageShortSender implements MessageSender {

    private static final Logger log = LoggerFactory.getLogger(AzureMessageShortSender.class);

    private AzureServiceBusConfig azureServiceBusConfig;

    public void setAzureServiceBusConfig(AzureServiceBusConfig azureServiceBusConfig) {
        this.azureServiceBusConfig = azureServiceBusConfig;
    }

    /**
     * 初始化短连接
     */
    private TopicClient openConnection(String topic) throws Exception {
        String connectionString = azureServiceBusConfig.getConnectionString();
        if (StringUtils.isBlank(connectionString) || StringUtils.isBlank(topic)) {
            return null;
        }
        ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString, topic);
        TopicClient topicClient = new TopicClient(connectionStringBuilder);
        log.info("TopicClient New Success");
        return topicClient;
    }

    /**
     * 发送消息
     */
    @Override
    public void send(String topic, String message) {
        TopicClient topicClient = null;
        try {
            Message messages = new Message(message);
            topicClient = this.openConnection(topic);
            if (topicClient != null) {
                topicClient.send(messages);
                log.info("TopicClient Send Success");
            }
        } catch (Exception e) {
            log.error("TopicClient Send Exception", e);
        } finally {
            try {
                topicClient.close();
                log.info("TopicClient Close Success");
            } catch (ServiceBusException e) {
                log.error("TopicClient Close Exception", e);
            }
        }
    }

    /**
     * 定时发送消息
     */
    @Override
    public void send(String topic, String message, Date sendDate) {
        TopicClient topicClient = null;
        try {
            Message messages = new Message(message);
            messages.setScheduledEnqueuedTimeUtc(Instant.ofEpochMilli(sendDate.getTime()));
            topicClient = this.openConnection(topic);
            if (topicClient != null) {
                topicClient.send(messages);
                log.info("TopicClient Send Success");
            }
        } catch (Exception e) {
            log.error("TopicClient Send Exception", e);
        } finally {
            try {
                topicClient.close();
                log.info("TopicClient Close Success");
            } catch (ServiceBusException e) {
                log.error("TopicClient Close Exception", e);
            }
        }
    }
}
