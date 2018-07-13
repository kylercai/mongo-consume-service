package com.msl.api.ods.api;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.TopicClient;
import com.microsoft.azure.servicebus.primitives.ClientConstants;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

public class AzureTest1 {

	public static void main(String[] args) {
		Scanner reader =new Scanner(System.in);
		System.out.println("messageSize:");
		int messageSize = Integer.valueOf(reader.next()) ;
		System.out.println("messageCounts:");
		int messageCounts = Integer.valueOf(reader.next());
//		System.out.println("batchSize:");
//		int batchSize = Integer.valueOf(reader.next());
		System.out.println("sendInterval in milliseconds:");
		int sendInterval = Integer.valueOf(reader.next());
		sendMessage(messageSize, messageCounts, sendInterval);
	}

	protected static void sendMessage(int messageSize, int messageCounts, int sendInterval) {

		String subscription = "process/subscriptions/process-subscription";
		//String connectionString = "Endpoint=sb://amasbssit.servicebus.chinacloudapi.cn/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=VVxfR6QAwHOchp/HNVxnpkzgnZfJ50UKsSu1qSW0yM0=";
	    String connectionString = "Endpoint=sb://sbkyler.servicebus.chinacloudapi.cn/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=BDU84CmwycMjbUdM978EOUcJVwId1SiVW5LqwDbsV8E=";

		//ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString, subscription);
		//QueueClient client;
		TopicClient topicClient;
		int batchSize = Math.min(100, ClientConstants.MAX_MESSAGE_LENGTH_BYTES / messageSize);
		
		try {
			ConnectionStringBuilder builder = new ConnectionStringBuilder(connectionString, "process");
			Duration timeout = builder.getOperationTimeout();
			//System.out.println("timeout in sec: " + timeout.getSeconds() + "; in nanosec: " + timeout.getNano());
			topicClient = new TopicClient(builder);
			//client = new QueueClient(connectionStringBuilder, ReceiveMode.PEEKLOCK);
			char[] buf = new char[messageSize];
			for (int i = 0; i < messageSize; i++) {
				buf[i] = '0';
			}
			long timeBefore = System.currentTimeMillis();
			ArrayList<IMessage> collection = new ArrayList<IMessage>();
			int looptimes = messageCounts/batchSize;
			for (int i = 0; i < looptimes; i++) {
				for ( int j = 0; j<batchSize; j++) {
					Message message = new Message("message " + i + ": " + String.valueOf(buf));
					collection.add(message);
				}
				topicClient.sendBatchAsync(collection);
				//topicClient.sendAsync(message);
				System.out.println("sent message " + new Integer(i*batchSize).toString() + " ~ " + new Integer((i+1)*batchSize-1).toString());
				if ( sendInterval > 0 ) {
					Thread.sleep(sendInterval);
				}
				collection.clear();
			}
			long timeAfter = System.currentTimeMillis();
			long duration = timeAfter - timeBefore;
			System.out.println("message size = " + messageSize + " bytes; message counts = " + messageCounts);
			System.out.println("send elapse time = " + duration + " milliSec");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("send messages done");

	}

}
