package com.yatharth.distributedurlshortener.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class RateLimitService {

    private static final int MAX_REQUESTS = 10;
    private static final Duration WINDOW = Duration.ofMinutes(1);

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
                String.valueOf(WINDOW.getSeconds())
        );

        return count != null && count <= MAX_REQUESTS;
    }
}