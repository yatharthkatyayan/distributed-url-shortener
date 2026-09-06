package com.yatharth.distributedurlshortener.service;

import com.yatharth.distributedurlshortener.entity.Url;
import com.yatharth.distributedurlshortener.exception.UrlNotFoundException;
import com.yatharth.distributedurlshortener.repository.UrlRepository;
import com.yatharth.distributedurlshortener.util.Base62Encoder;
import com.yatharth.distributedurlshortener.util.SnowflakeIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private static final Logger logger =
            LoggerFactory.getLogger(UrlService.class);

    public UrlService(
            UrlRepository urlRepository,
            SnowflakeIdGenerator snowflakeIdGenerator) {
        this.urlRepository = urlRepository;
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    public Url createShortUrl(String originalUrl) {

        long id = snowflakeIdGenerator.generateId();

        String shortCode = Base62Encoder.encode(id);

        Url url = new Url();
        url.setId(id);
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);

        return urlRepository.save(url);
    }

    @Cacheable(value = "urls", key = "#shortCode")
    public String getOriginalUrlByShortCode(String shortCode) {
        logger.info(
                "Cache MISS - querying PostgreSQL for shortCode={}",
                shortCode
        );
        return urlRepository.findByShortCode(shortCode)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new UrlNotFoundException(
                        "URL not found for short code: " + shortCode
                ));
    }

}