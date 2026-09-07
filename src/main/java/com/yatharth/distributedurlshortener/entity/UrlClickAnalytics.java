package com.yatharth.distributedurlshortener.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "url_click_analytics")
public class UrlClickAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long urlId;

    private String shortCode;

    private Instant clickedAt;

    private Instant processedAt;

    public UrlClickAnalytics() {
    }

    public UrlClickAnalytics(
            Long urlId,
            String shortCode,
            Instant clickedAt,
            Instant processedAt) {

        this.urlId = urlId;
        this.shortCode = shortCode;
        this.clickedAt = clickedAt;
        this.processedAt = processedAt;
    }

    public Long getId() {
        return id;
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