package com.yatharth.distributedurlshortener.service;

import com.yatharth.distributedurlshortener.entity.Url;
import com.yatharth.distributedurlshortener.exception.UrlNotFoundException;
import com.yatharth.distributedurlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

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

    public Url getUrlByShortCode(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(
                "URL not found for short code: " + shortCode));
    }

}