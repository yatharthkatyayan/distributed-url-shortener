package com.yatharth.distributedurlshortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class RateLimitService {

    @Value("${app.rate-limit.max-requests:10}")
    private int maxRequests;

    @Value("${app.rate-limit.window-minutes:1}")
    private int windowMinutes;

    private final StringRedisTemplate redisTemplate;

    private final DefaultRedisScript<Long> rateLimitScript;

    public RateLimitService(StringRedisTemplate redisTemplate) {

        this.redisTemplate = redisTemplate;

        this.rateLimitScript = new DefaultRedisScript<>(
                """
                local current = redis.call('INCR', KEYS[1])

                if current == 1 then
                    redis.call('EXPIRE', KEYS[1], ARGV[1])
                end

                return current
                """,
                Long.class
        );
    }

    public boolean isAllowed(String clientKey) {

        String key = "rate-limit:" + clientKey;

        Long count = redisTemplate.execute(
                rateLimitScript,
                List.of(key),
                String.valueOf(Duration.ofMinutes(windowMinutes).getSeconds())
        );

        return count != null && count <= maxRequests;
    }
}