package com.msl.common.utilities.message.azure;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.msl.common.utilities.message.MessageReceiver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class AbstractAzureMessageReceiver implements MessageReceiver {

    private static final Log logger = LogFactory.getLog(AbstractAzureMessageReceiver.class);

    private SubscriptionClient subscriptionClient = null;

    @Autowired
    private AzureServiceBusConfig azureServiceBusConfig;

    @PostConstruct
    private void initConnection() {
        String connectionString = azureServiceBusConfig.getConnectionString();
        String subscription = signQueueName();
        if (connectionString == null || subscription == null || "".equals(connectionString) || "".equals(subscription)) {
            return;
        }
        try {
            ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString, subscription);
            subscriptionClient = new SubscriptionClient(connectionStringBuilder, ReceiveMode.PEEKLOCK);
            subscriptionClient.registerMessageHandler(new IMessageHandler() {
                @Override
                public CompletableFuture<Void> onMessageAsync(IMessage iMessage) {
                    receive(new String(iMessage.getBody(), UTF_8));
                    return subscriptionClient.completeAsync(iMessage.getLockToken()).thenRun(() -> logger.info("SubscriptionClient OnMessageAsync Success"));
                }

                @Override
                public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
                    logger.error(exceptionPhase.toString(), throwable);
                }
            }, new MessageHandlerOptions(1, false, Duration.ofMinutes(1)));
        } catch (Exception e) {
            logger.error("AbstractAzureMessageReceiver InitConnection Exception", e);
        }
    }

    @PreDestroy
    private void destroyConnection() {
        try {
            subscriptionClient.close();
        } catch (Exception e) {
            logger.error("AbstractAzureMessageReceiver DestroyConnection Exception", e);
        }
    }
}