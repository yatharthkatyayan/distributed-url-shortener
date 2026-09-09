package com.yatharth.distributedurlshortener.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "url_click_analytics")
public class UrlClickAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID eventId;

    private Long urlId;

    private String shortCode;

    private Instant clickedAt;

    private Instant processedAt;

    public UrlClickAnalytics() {
    }

    public UrlClickAnalytics(
            UUID eventId,
            Long urlId,
            String shortCode,
            Instant clickedAt,
            Instant processedAt) {

        this.eventId = eventId;
        this.urlId = urlId;
        this.shortCode = shortCode;
        this.clickedAt = clickedAt;
        this.processedAt = processedAt;
    }

    public Long getId() {
        return id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public Long getUrlId() {
        return urlId;
    }

    public String getShortCode() {
        return shortCode;
    }

    public Instant getClickedAt() {
        return clickedAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}