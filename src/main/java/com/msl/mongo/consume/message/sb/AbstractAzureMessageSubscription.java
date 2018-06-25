package com.msl.mongo.consume.message.sb;

import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class AbstractAzureMessageSubscription {

    private static final Log logger = LogFactory.getLog(AbstractAzureMessageSubscription.class);
    protected static boolean printFetch = false;
    protected int concurrentcall = 10;

    private SubscriptionClient subscriptionClient = null;
//    private String connectionString = "Endpoint=sb://amasbuat.servicebus.chinacloudapi.cn/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=cyLXdxCWLAPXK5P4/EjqwJ9qHRvfoSMBOsgE//doGrY=";
//    protected String connectionString = "Endpoint=sb://sbkyler.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=RMvyYTi+2KRWCE/2GSzPE/q/NIqyYqYmRtL5i1dmOfg=";
    protected String connectionString = "Endpoint=sb://sbkyler.servicebus.chinacloudapi.cn/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=BDU84CmwycMjbUdM978EOUcJVwId1SiVW5LqwDbsV8E=";

    protected abstract String signQueueName();

    protected abstract void receive(String message);

    @PostConstruct
    protected void initConnection() {
        String subscription = signQueueName();
        if (connectionString == null || subscription == null || "".equals(connectionString) || "".equals(subscription)) {
            return;
        }
        try {
            ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString, subscription);
            subscriptionClient = new SubscriptionClient(connectionStringBuilder, ReceiveMode.PEEKLOCK);
            subscriptionClient.registerMessageHandler(new IMessageHandler() {
            	long count = 0;
            	long startFetchTime = 0;
            	 
                @Override
                public CompletableFuture<Void> onMessageAsync(IMessage iMessage) {
                    //receive(new String(iMessage.getBody(), UTF_8));
                    count++;
                    if ( count ==1 ) {
                    	startFetchTime = System.currentTimeMillis();
                    }
                    
                	long currentTime = System.currentTimeMillis();
                	long duration = currentTime - startFetchTime;
                	
                	if ( printFetch ) {
                    	System.out.println("Fetched msg=" + new String(iMessage.getBody()).substring(0,  30) + "... count= " + count + ", cost " + duration + " millSec. concurrent="+concurrentcall);
                	} else {
                    	System.out.println("Fetched msg... count= " + count + ", cost " + duration + " millSec. concurrent="+concurrentcall);
                	}
                    //return subscriptionClient.completeAsync(iMessage.getLockToken()).thenRun(() -> logger.info("SubscriptionClient OnMessageAsync Success"));
                    return subscriptionClient.completeAsync(iMessage.getLockToken());
                }

                @Override
                public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
                    logger.error(exceptionPhase.toString(), throwable);
                }
            }, new MessageHandlerOptions(concurrentcall, false, Duration.ofMillis(1)));
        } catch (Exception e) {
            atomicInteger.set(atomicInteger.get() + 1);
            logger.error("AbstractAzureMessageReceiver InitConnection Exception", e);
        }
    }

    protected AtomicInteger atomicInteger = new AtomicInteger(0);

    @PreDestroy
    private void destroyConnection() {
        try {
            subscriptionClient.close();
        } catch (Exception e) {
            logger.error("AbstractAzureMessageReceiver DestroyConnection Exception", e);
        }
    }
}