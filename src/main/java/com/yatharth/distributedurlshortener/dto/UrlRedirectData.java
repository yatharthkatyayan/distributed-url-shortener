package com.yatharth.distributedurlshortener.dto;

public record UrlRedirectData(
        Long urlId,
        String shortCode,
        String originalUrl
) {
}
