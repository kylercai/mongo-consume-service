package com.msl.common.utilities.message;

import java.util.Date;


public interface MessageSender {
    void send(String topic, String message);

    void send(String topic, String message, Date sendDate);
}
