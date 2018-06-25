package com.msl.common.utilities.message.rabbit;

import com.msl.common.utilities.message.MessageReceiver;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

public abstract class AbstractRabbitMessageReceiver implements MessageReceiver, ChannelAwareMessageListener {

    @Autowired
    private ConnectionFactory connectionFactory;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        this.receive(new String(message.getBody(), "UTF-8"));
    }

    @Bean
    SimpleMessageListenerContainer processContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(signQueueName());
        container.setMessageListener(new MessageListenerAdapter(this));
        return container;
    }

}