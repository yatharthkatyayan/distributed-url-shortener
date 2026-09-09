package com.yatharth.distributedurlshortener.event;

import java.time.Instant;
import java.util.UUID;

public record UrlClickedEvent(
        UUID eventId,
        Long urlId,
        String shortCode,
        Instant clickedAt
) {
}