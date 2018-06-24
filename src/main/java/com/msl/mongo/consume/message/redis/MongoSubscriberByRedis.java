package com.msl.mongo.consume.message.redis;

import com.msl.mongo.consume.message.Receive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MongoSubscriberByRedis {

//    如何发送消息：stringRedisTemplate.convertAndSend("cosmos_topic", messageJson);

    @Autowired
    private Receive receive;

    protected void receive(String messageJson) {
        receive.receive(messageJson);
    }
}
