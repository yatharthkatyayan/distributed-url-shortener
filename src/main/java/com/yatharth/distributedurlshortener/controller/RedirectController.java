package com.yatharth.distributedurlshortener.controller;

import com.yatharth.distributedurlshortener.dto.UrlRedirectData;
import com.yatharth.distributedurlshortener.event.UrlClickedEvent;
import com.yatharth.distributedurlshortener.producer.UrlClickEventProducer;
import com.yatharth.distributedurlshortener.service.UrlService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

@RestController
public class RedirectController {

    private final UrlService urlService;
    private final UrlClickEventProducer urlClickEventProducer;
    private final MeterRegistry meterRegistry;

    public RedirectController(
            UrlService urlService,
            UrlClickEventProducer urlClickEventProducer,
            MeterRegistry meterRegistry) {

        this.urlService = urlService;
        this.urlClickEventProducer = urlClickEventProducer;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {

        UrlRedirectData redirectData =
                urlService.getUrlForRedirect(shortCode);

        meterRegistry
                .counter("url.redirect.total")
                .increment();

        UrlClickedEvent event = new UrlClickedEvent(
                UUID.randomUUID(),
                redirectData.urlId(),
                redirectData.shortCode(),
                Instant.now()
        );

        urlClickEventProducer.publishUrlClicked(event);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(redirectData.originalUrl()))
                .build();
    }
}
