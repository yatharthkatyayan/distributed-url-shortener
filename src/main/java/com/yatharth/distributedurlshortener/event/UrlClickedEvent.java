package com.yatharth.distributedurlshortener.event;

import java.time.Instant;

public record UrlClickedEvent(
        Long urlId,
        String shortCode,
        Instant clickedAt
) {
}