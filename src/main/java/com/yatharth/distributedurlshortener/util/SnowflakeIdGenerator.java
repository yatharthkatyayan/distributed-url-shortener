package com.yatharth.distributedurlshortener.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGenerator {

    private static final long CUSTOM_EPOCH = 1767225600000L;

    private static final long WORKER_ID_BITS = 10;
    private static final long SEQUENCE_BITS = 12;

    private static final long MAX_WORKER_ID =
            (1L << WORKER_ID_BITS) - 1;

    private static final long MAX_SEQUENCE =
            (1L << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    private static final long TIMESTAMP_SHIFT =
            SEQUENCE_BITS + WORKER_ID_BITS;

    private final long workerId;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowflakeIdGenerator(
            @Value("${snowflake.worker-id:1}") long workerId) {

        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException(
                    "Worker ID must be between 0 and " + MAX_WORKER_ID
            );
        }

        this.workerId = workerId;
    }

    public synchronized long generateId() {

        long currentTimestamp = currentTimestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException(
                    "Clock moved backwards. Refusing to generate ID."
            );
        }

        if (currentTimestamp == lastTimestamp) {

            sequence = (sequence + 1) & MAX_SEQUENCE;

            if (sequence == 0) {
                currentTimestamp = waitForNextMillis(lastTimestamp);
            }

        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return (currentTimestamp << TIMESTAMP_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    private long currentTimestamp() {
        return System.currentTimeMillis() - CUSTOM_EPOCH;
    }

    private long waitForNextMillis(long lastTimestamp) {

        long timestamp = currentTimestamp();

        while (timestamp <= lastTimestamp) {
            timestamp = currentTimestamp();
        }

        return timestamp;
    }
}