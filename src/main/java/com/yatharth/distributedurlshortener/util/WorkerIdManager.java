package com.yatharth.distributedurlshortener.util;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Component
public class WorkerIdManager {

    private static final int MAX_WORKER_ID = 1023;
    private static final Duration LEASE_DURATION =
            Duration.ofSeconds(30);

    private final StringRedisTemplate redisTemplate;
    private static final Logger logger =
            LoggerFactory.getLogger(WorkerIdManager.class);

    private static final long RENEWAL_INTERVAL_MS = 10_000;

    private final String instanceId =
            UUID.randomUUID().toString();

    private long workerId = -1;

    public WorkerIdManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long acquireWorkerId() {

        for (int id = 0; id <= MAX_WORKER_ID; id++) {

            String key = "snowflake:worker:" + id;

            Boolean acquired = redisTemplate
                    .opsForValue()
                    .setIfAbsent(
                            key,
                            instanceId,
                            LEASE_DURATION
                    );

            if (Boolean.TRUE.equals(acquired)) {
                workerId = id;

                logger.info(
                        "Acquired Snowflake worker ID: {} for instance {}",
                        id,
                        instanceId
                );

                return id;
            }
        }

        throw new IllegalStateException(
                "No available Snowflake worker ID"
        );
    }

    @Scheduled(fixedRate = RENEWAL_INTERVAL_MS)
    public void renewLease() {

        if (workerId == -1) {
            return;
        }

        String key = "snowflake:worker:" + workerId;

        String currentOwner = redisTemplate
                .opsForValue()
                .get(key);

        if (instanceId.equals(currentOwner)) {

            redisTemplate.expire(
                    key,
                    LEASE_DURATION
            );

            logger.debug(
                    "Renewed worker ID lease: {}",
                    workerId
            );

        } else {

            logger.error(
                    "Lost ownership of Snowflake worker ID: {}",
                    workerId
            );
        }
    }

    @PreDestroy
    public void releaseWorkerId() {

        if (workerId == -1) {
            return;
        }

        String key = "snowflake:worker:" + workerId;

        String script = """
            if redis.call('GET', KEYS[1]) == ARGV[1] then
                return redis.call('DEL', KEYS[1])
            else
                return 0
            end
            """;

        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                List.of(key),
                instanceId
        );

        if (Long.valueOf(1).equals(result)) {

            logger.info(
                    "Released Snowflake worker ID: {}",
                    workerId
            );

        } else {

            logger.info(
                    "Worker ID {} was not released because this instance no longer owns it",
                    workerId
            );
        }
    }

}