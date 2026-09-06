package com.yatharth.distributedurlshortener.service;

import com.yatharth.distributedurlshortener.entity.Url;
import com.yatharth.distributedurlshortener.exception.UrlNotFoundException;
import com.yatharth.distributedurlshortener.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private static final Logger logger =
            LoggerFactory.getLogger(UrlService.class);

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url createShortUrl(String originalUrl) {

        String shortCode = generateShortCode();

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);

        return urlRepository.save(url);
    }

    private String generateShortCode() {
        return java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 7);
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