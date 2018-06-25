package com.msl.common.utilities.message;

public interface MessageReceiver {
    void receive(String message);

    String signQueueName();
}