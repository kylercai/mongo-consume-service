package com.msl.mongo.consume.message.sb;

import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;

public class ServiceBusMessageReceiver {
	
	protected void receiveMessage() {
	       String subscription = "ckqueue";
	       String connectionString = "Endpoint=sb://sbkyler.servicebus.chinacloudapi.cn/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=BDU84CmwycMjbUdM978EOUcJVwId1SiVW5LqwDbsV8E=";
	       
	       ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString, subscription);

			QueueClient client;
			try {
				client = new QueueClient(connectionStringBuilder, ReceiveMode.PEEKLOCK);
			} catch (InterruptedException | ServiceBusException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("send messages done");
		
	}

	public static void main(String[] args) {
	}

}
