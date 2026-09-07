package com.yatharth.distributedurlshortener.consumer;

import com.yatharth.distributedurlshortener.entity.UrlClickAnalytics;
import com.yatharth.distributedurlshortener.event.UrlClickedEvent;
import com.yatharth.distributedurlshortener.repository.UrlClickAnalyticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UrlClickedEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(UrlClickedEventConsumer.class);

    private final UrlClickAnalyticsRepository analyticsRepository;

    public UrlClickedEventConsumer(
            UrlClickAnalyticsRepository analyticsRepository) {

        this.analyticsRepository = analyticsRepository;
    }

    @KafkaListener(
            topics = "url-click-events",
            groupId = "url-click-analytics-group",
            containerFactory = "clickEventKafkaListenerContainerFactory"
    )
    public void consume(UrlClickedEvent event) {

        UrlClickAnalytics analytics = new UrlClickAnalytics(
                event.urlId(),
                event.shortCode(),
                event.clickedAt(),
                Instant.now()
        );

        analyticsRepository.save(analytics);

        log.info(
                "Persisted URL click: urlId={}, shortCode={}, clickedAt={}",
                event.urlId(),
                event.shortCode(),
                event.clickedAt()
        );
    }
}