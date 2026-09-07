package com.yatharth.distributedurlshortener.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "url_analytics")
public class UrlAnalytics {

    @Id
    private Long id;

    private String shortCode;

    private String eventType;

    private String originalUrl;

    private Instant createdAt;

    private Instant processedAt;

    public UrlAnalytics() {
    }

    public UrlAnalytics(
            Long id,
            String shortCode,
            String eventType,
            String originalUrl,
            Instant createdAt,
            Instant processedAt) {

        this.id = id;
        this.shortCode = shortCode;
        this.eventType = eventType;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
    }

    public Long getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getEventType() {
        return eventType;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }
}