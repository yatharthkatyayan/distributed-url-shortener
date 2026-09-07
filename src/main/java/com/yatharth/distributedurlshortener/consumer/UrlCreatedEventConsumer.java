package com.yatharth.distributedurlshortener.consumer;

import com.yatharth.distributedurlshortener.entity.UrlAnalytics;
import com.yatharth.distributedurlshortener.event.UrlCreatedEvent;
import com.yatharth.distributedurlshortener.repository.UrlAnalyticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UrlCreatedEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(UrlCreatedEventConsumer.class);

    private final UrlAnalyticsRepository analyticsRepository;

    public UrlCreatedEventConsumer(
            UrlAnalyticsRepository analyticsRepository) {
        this.analyticsRepository = analyticsRepository;
    }

    @KafkaListener(
            topics = "url-events",
            groupId = "url-analytics-group"
    )
    public void consume(UrlCreatedEvent event) {

        if (analyticsRepository.existsById(event.urlId())) {

            log.info(
                    "Duplicate event ignored: urlId={}, shortCode={}",
                    event.urlId(),
                    event.shortCode()
            );

            return;
        }

        UrlAnalytics analytics = new UrlAnalytics(
                event.urlId(),
                event.shortCode(),
                "URL_CREATED",
                event.originalUrl(),
                event.createdAt(),
                Instant.now()
        );

        analyticsRepository.save(analytics);

        log.info(
                "Processed URL created event: urlId={}, shortCode={}",
                event.urlId(),
                event.shortCode()
        );
    }
}