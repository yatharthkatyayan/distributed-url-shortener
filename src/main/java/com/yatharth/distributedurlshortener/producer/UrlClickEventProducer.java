package com.yatharth.distributedurlshortener.producer;

import com.yatharth.distributedurlshortener.event.UrlClickedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UrlClickEventProducer {

    private static final Logger log =
            LoggerFactory.getLogger(UrlClickEventProducer.class);

    private static final String URL_CLICK_EVENTS_TOPIC =
            "url-click-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UrlClickEventProducer(
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUrlClicked(UrlClickedEvent event) {

        try {
            kafkaTemplate.send(
                    URL_CLICK_EVENTS_TOPIC,
                    event.shortCode(),
                    event
            ).whenComplete((result, exception) -> {

                if (exception != null) {
                    log.warn(
                            "Failed to publish URL click event: eventId={}, shortCode={}",
                            event.eventId(),
                            event.shortCode(),
                            exception
                    );
                }
            });

        } catch (Exception exception) {

            log.warn(
                    "Failed to initiate URL click event publishing: eventId={}, shortCode={}",
                    event.eventId(),
                    event.shortCode(),
                    exception
            );
        }
    }
}