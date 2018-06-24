package com.msl.mongo.consume.message.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisMessageConfig {

    private static String topicName = "cosmos_topic";

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(connectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic(topicName));
        return redisMessageListenerContainer;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(MongoSubscriberByRedis mongoSubscriberByRedis) {
        return new MessageListenerAdapter(mongoSubscriberByRedis, "receive");
    }

}