package com.yatharth.distributedurlshortener.controller;

import com.yatharth.distributedurlshortener.dto.CreateShortUrlRequest;
import com.yatharth.distributedurlshortener.dto.CreateShortUrlResponse;
import com.yatharth.distributedurlshortener.entity.Url;
import com.yatharth.distributedurlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    public ResponseEntity<CreateShortUrlResponse> createShortUrl(
            @Valid @RequestBody CreateShortUrlRequest request) {

        Url url = urlService.createShortUrl(request.originalUrl());

        CreateShortUrlResponse response = new CreateShortUrlResponse(
                url.getShortCode(),
                "http://localhost:8080/" + url.getShortCode()
        );

        return ResponseEntity.ok(response);
    }
}