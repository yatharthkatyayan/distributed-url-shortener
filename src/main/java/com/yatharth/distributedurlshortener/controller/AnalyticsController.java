package com.yatharth.distributedurlshortener.controller;

import com.yatharth.distributedurlshortener.dto.UrlAnalyticsResponse;
import com.yatharth.distributedurlshortener.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(
            AnalyticsService analyticsService) {

        this.analyticsService = analyticsService;
    }

    @GetMapping("/{shortCode}/analytics")
    public UrlAnalyticsResponse getAnalytics(
            @PathVariable String shortCode) {

        return analyticsService.getAnalytics(shortCode);
    }
}