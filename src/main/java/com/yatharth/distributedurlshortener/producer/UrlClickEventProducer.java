package com.yatharth.distributedurlshortener.producer;

import com.yatharth.distributedurlshortener.event.UrlClickedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UrlClickEventProducer {

    private static final String URL_CLICK_EVENTS_TOPIC =
            "url-click-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UrlClickEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUrlClicked(UrlClickedEvent event) {

        kafkaTemplate.send(
                URL_CLICK_EVENTS_TOPIC,
                event.shortCode(),
                event
        );
    }
}