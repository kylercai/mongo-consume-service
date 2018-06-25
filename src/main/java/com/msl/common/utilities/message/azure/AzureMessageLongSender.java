package com.msl.common.utilities.message.azure;

import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ClientConstants;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.RetryExponential;
import com.msl.common.utilities.message.MessageSender;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Azure消息发送（长连接）
 */
public class AzureMessageLongSender implements MessageSender {

    private static final Logger log = LoggerFactory.getLogger(AzureMessageLongSender.class);

    private AzureServiceBusConfig azureServiceBusConfig;

    public void setAzureServiceBusConfig(AzureServiceBusConfig azureServiceBusConfig) {
        this.azureServiceBusConfig = azureServiceBusConfig;
    }

    private Map<String, String> topicNamePool = new ConcurrentHashMap<>();
    private Map<String, TopicClient> topicClientPool = new ConcurrentHashMap<>();


    /**
     * 初始化连接池
     */
    @PostConstruct
    private void initConnection() {
        Map<String, String> topicNames = azureServiceBusConfig.getTopics();
        if (topicNames != null) {
            for (Map.Entry<String, String> entry : azureServiceBusConfig.getTopics().entrySet()) {
                try {
                    openConnection(entry.getValue());
                } catch (Exception e) {
                    log.error("Azure InitConnection Exception", e);
                }
            }
        }
    }

    /**
     * 销毁所有连接
     */
    @PreDestroy
    private void destroyConnection() {
        for (Map.Entry<String, TopicClient> entry : topicClientPool.entrySet()) {
            try {
                entry.getValue().close();
                log.info("TopicClient Close Success");
            } catch (Exception e) {
                log.error("Azure DestroyConnection Exception", e);
            }
        }
    }

    /**
     * 初始化长连接
     */
    private void openConnection(String topic) throws Exception {
        String topicName = topicNamePool.get(topic);
        TopicClient topicClient = topicClientPool.get(topic);
        if (StringUtils.isBlank(topicName) && topicClient == null) {
            String connectionString = azureServiceBusConfig.getConnectionString();
            if (StringUtils.isBlank(connectionString) || StringUtils.isBlank(topic)) {
                return;
            }
            topicNamePool.put(topic, topic);
            ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString, topic);
            connectionStringBuilder.setOperationTimeout(Duration.ofDays(48));
            connectionStringBuilder.setRetryPolicy(new RetryExponential(Duration.ofSeconds(0), Duration.ofDays(48), ClientConstants.DEFAULT_MAX_RETRY_COUNT, ClientConstants.DEFAULT_RETRY));
            topicClient = new TopicClient(connectionStringBuilder);
            topicClientPool.put(topic, topicClient);
            log.info("TopicClient New Success");
        }
    }

    /**
     * 获取连接池连接
     */
    private TopicClient getTopicClient(String topic) throws Exception {
        TopicClient topicClient = null;
        do {
            openConnection(topic);
            topicClient = topicClientPool.get(topic);
        } while (topicClient == null);
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
            topicClient = this.getTopicClient(topic);
            if (topicClient != null) {
                topicClient.send(messages);
                log.info("TopicClient Send Success");
            }
        } catch (Exception e) {
            log.error("TopicClient Send Exception", e);
            reConnection(topic);
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
            topicClient = this.getTopicClient(topic);
            if (topicClient != null) {
                topicClient.send(messages);
                log.info("TopicClient Send Success");
            }
        } catch (Exception e) {
            log.error("TopicClient Send Exception", e);
            reConnection(topic);
        }
    }

    /**
     * 重连接
     */
    private void reConnection(String topic) {
        try {
            String topicName = topicNamePool.get(topic);
            TopicClient topicClient = this.getTopicClient(topic);
            if (topicClient != null) {
                topicClient.close();
                log.info("TopicClient Close Success");
            }
            if (StringUtils.isNotBlank(topicName) && topicClient != null) {
                topicNamePool.remove(topic);
                topicClientPool.remove(topic);
            }
            openConnection(topic);
        } catch (Exception e) {
            log.error("TopicClient ReConnection Exception", e);
        }
    }
}
