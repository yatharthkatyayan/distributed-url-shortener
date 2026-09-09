package com.yatharth.distributedurlshortener.repository;

import com.yatharth.distributedurlshortener.entity.UrlClickAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UrlClickAnalyticsRepository
        extends JpaRepository<UrlClickAnalytics, Long> {

    long countByShortCode(String shortCode);
    boolean existsByEventId(UUID eventId);
}