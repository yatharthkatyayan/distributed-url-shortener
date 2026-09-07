package com.yatharth.distributedurlshortener.producer;

import com.yatharth.distributedurlshortener.event.UrlCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UrlEventProducer {

    private static final String URL_EVENTS_TOPIC =
            "url-events";

    private final KafkaTemplate<String, UrlCreatedEvent> kafkaTemplate;

    public UrlEventProducer(
            KafkaTemplate<String, UrlCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUrlCreated(UrlCreatedEvent event) {

        kafkaTemplate.send(
                URL_EVENTS_TOPIC,
                event.shortCode(),
                event
        );
    }
}