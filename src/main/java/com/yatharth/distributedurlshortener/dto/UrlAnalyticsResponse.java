package com.yatharth.distributedurlshortener.dto;

public record UrlAnalyticsResponse(
        String shortCode,
        long totalClicks
) {
}