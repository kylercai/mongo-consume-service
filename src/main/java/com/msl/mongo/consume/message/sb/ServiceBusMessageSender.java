package com.msl.mongo.consume.message.sb;

import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;

import java.sql.Date;

import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

public class ServiceBusMessageSender extends AbstractAzureMessageSubscription {

	@Override
	protected String signQueueName() {
		// TODO Auto-generated method stub
        return "ckqueue";
	}

	@Override
	protected void receive(String message) {
		// TODO Auto-generated method stub

	}
	
	protected void sendMessage(int messageSize, int messageCounts) {
       String subscription = signQueueName();
       ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString, subscription);

		QueueClient client;
		try {
			client = new QueueClient(connectionStringBuilder, ReceiveMode.PEEKLOCK);
			char[] buf = new char[messageSize];
			for (int i=0; i<messageSize; i++){buf[i] = '0';};
			
			long timeBefore = System.currentTimeMillis();
			for ( int i=0; i<messageCounts; i++) {
				Message message = new Message( "message " + i + ": " + String.valueOf(buf) );
				client.sendAsync(message);
				System.out.println("sent message " + i);
			}
			long timeAfter = System.currentTimeMillis();
			long duration = timeAfter - timeBefore;
			System.out.println("message size = " + messageSize + " bytes; message counts = " + messageCounts);
			System.out.println("send elapse time = " + duration + " milliSec");
		} catch (InterruptedException | ServiceBusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("send messages done");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServiceBusMessageSender sender = new ServiceBusMessageSender();
		if ( args.length < 3 ) {
			System.out.println("Usage: ServiceBusMessageSender message_size message_count");
		} else {
			System.out.println("message_size = " + args[1]);
			System.out.println("message count = " + args[2]);
			sender.sendMessage(Integer.valueOf(args[1]),Integer.valueOf(args[2]));
		}
	}

}
