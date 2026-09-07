package com.yatharth.distributedurlshortener.service;

import com.yatharth.distributedurlshortener.dto.UrlAnalyticsResponse;
import com.yatharth.distributedurlshortener.repository.UrlClickAnalyticsRepository;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    private final UrlClickAnalyticsRepository analyticsRepository;

    public AnalyticsService(
            UrlClickAnalyticsRepository analyticsRepository) {

        this.analyticsRepository = analyticsRepository;
    }

    public UrlAnalyticsResponse getAnalytics(String shortCode) {

        long totalClicks =
                analyticsRepository.countByShortCode(shortCode);

        return new UrlAnalyticsResponse(
                shortCode,
                totalClicks
        );
    }
}