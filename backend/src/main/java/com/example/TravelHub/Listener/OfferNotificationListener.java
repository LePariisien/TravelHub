package com.example.TravelHub.Listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class OfferNotificationListener implements MessageListener {

    private final RedisMessageListenerContainer listenerContainer;

    public OfferNotificationListener(RedisMessageListenerContainer listenerContainer) {
        this.listenerContainer = listenerContainer;
    }

    @PostConstruct
    public void subscribe() {
        listenerContainer.addMessageListener(this, new ChannelTopic("offers:new"));
        System.out.println("[OfferNotificationListener] Subscribed to offers:new");
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("NOUVELLE OFFRE PUBLIÉE : " + message.toString());
    }
}
