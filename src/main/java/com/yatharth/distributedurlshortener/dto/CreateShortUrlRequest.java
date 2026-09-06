package com.yatharth.distributedurlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateShortUrlRequest(
        @NotBlank
        @Size(max = 2048)
        String originalUrl
) {}
