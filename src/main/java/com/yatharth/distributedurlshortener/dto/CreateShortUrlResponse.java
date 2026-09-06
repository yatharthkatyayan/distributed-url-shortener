package com.yatharth.distributedurlshortener.dto;

public record CreateShortUrlResponse(
        String shortCode,
        String shortUrl
) {}
