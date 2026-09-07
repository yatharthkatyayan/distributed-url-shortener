package com.yatharth.distributedurlshortener.event;

import java.time.Instant;

public record UrlCreatedEvent(
        Long urlId,
        String shortCode,
        String originalUrl,
        Instant createdAt
) {
}