package com.msl.mongo.consume.message.sb;

import com.msl.mongo.consume.message.Receive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

//@Component
public class MongoSubscriberByAzure extends AbstractAzureMessageSubscription {

    @Autowired
    private Receive receive;

    @Override
    protected String signQueueName() {
        //return "cosmos_topic/subscriptions/cosmos_subscription";
        return "ckqueue";
    }

    @Override
    protected void receive(String messageJson) {
        //receive.receive(messageJson);
    	//System.out.println(messageJson.substring(0, 30));
    }
    
    MongoSubscriberByAzure(int concurrent, boolean printMessage) {
    	concurrentcall = concurrent;
    	printFetch = printMessage;
    	initConnection();
    }
    
	public static void main(String[] args) {
		if ( args.length < 2 ) {
			System.out.println("Usage: xxxx concurrent [if_print_received_message:true/false]");
		} else {
			int concurrent = Integer.parseInt(args[1]);
			boolean printMessage = false;
			if ( args.length >= 3 ) {
				if ( args[2].equals("true")) {
					printMessage = true;
				}
			}
			MongoSubscriberByAzure subscriber = new MongoSubscriberByAzure(concurrent, printMessage);
		}
	}
}
