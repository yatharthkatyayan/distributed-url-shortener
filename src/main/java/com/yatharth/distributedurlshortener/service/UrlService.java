package com.yatharth.distributedurlshortener.service;

import com.yatharth.distributedurlshortener.dto.UrlRedirectData;
import com.yatharth.distributedurlshortener.entity.Url;
import com.yatharth.distributedurlshortener.event.UrlCreatedEvent;
import com.yatharth.distributedurlshortener.exception.UrlNotFoundException;
import com.yatharth.distributedurlshortener.producer.UrlClickEventProducer;
import com.yatharth.distributedurlshortener.producer.UrlEventProducer;
import com.yatharth.distributedurlshortener.repository.UrlRepository;
import com.yatharth.distributedurlshortener.util.Base62Encoder;
import com.yatharth.distributedurlshortener.util.SnowflakeIdGenerator;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final UrlEventProducer urlEventProducer;
    private static final Logger logger =
            LoggerFactory.getLogger(UrlService.class);
    private final UrlClickEventProducer urlClickEventProducer;
    private final MeterRegistry meterRegistry;

    public UrlService(
            UrlRepository urlRepository,
            SnowflakeIdGenerator snowflakeIdGenerator,
            UrlEventProducer urlEventProducer,
            UrlClickEventProducer urlClickEventProducer,
            MeterRegistry meterRegistry) {

        this.urlRepository = urlRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
        this.urlEventProducer = urlEventProducer;
        this.urlClickEventProducer = urlClickEventProducer;
        this.meterRegistry = meterRegistry;
    }
    public Url createShortUrl(String originalUrl) {

        long id = snowflakeIdGenerator.generateId();

        String shortCode = Base62Encoder.encode(id);

        Url url = new Url();
        url.setId(id);
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);

        Url savedUrl = urlRepository.save(url);

        meterRegistry
                .counter("url.creation.total")
                .increment();

        UrlCreatedEvent event = new UrlCreatedEvent(
                savedUrl.getId(),
                savedUrl.getShortCode(),
                savedUrl.getOriginalUrl(),
                savedUrl.getCreatedAt()
                        .toInstant(java.time.ZoneOffset.UTC)
        );

        urlEventProducer.publishUrlCreated(event);

        return savedUrl;
    }

    @Cacheable(value = "urls", key = "#shortCode")
    public UrlRedirectData getUrlForRedirect(String shortCode) {

        logger.info(
                "Cache MISS - querying PostgreSQL for shortCode={}",
                shortCode
        );

        return urlRepository.findByShortCode(shortCode)
                .map(url -> new UrlRedirectData(
                        url.getId(),
                        url.getShortCode(),
                        url.getOriginalUrl()
                ))
                .orElseThrow(() -> new UrlNotFoundException(
                        "URL not found for short code: " + shortCode
                ));
    }

}