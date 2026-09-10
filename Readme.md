# Distributed URL Shortener

A production-oriented distributed URL shortener built with **Java, Spring Boot, PostgreSQL, Redis, Kafka, Docker, and Nginx**.

The project focuses on distributed-system fundamentals such as horizontal scaling, distributed caching, asynchronous event processing, idempotency, rate limiting, failure handling, and load balancing.

---

## Features

- Short URL generation using a **Snowflake-style distributed ID generator + Base62 encoding**
- PostgreSQL as the source of truth
- Redis caching for high-frequency redirect lookups
- Asynchronous click analytics using Apache Kafka
- Kafka consumer groups with **4-way parallel click processing**
- Idempotent click-event processing using unique event IDs
- Kafka retry handling and Dead Letter Topic (DLT)
- Distributed rate limiting using Redis + atomic Lua scripting
- Horizontal scaling with multiple Spring Boot replicas
- Nginx load balancing
- Docker Compose-based local deployment
- Application readiness and dependency health checks
- Spring Boot Actuator metrics and health endpoints
- k6 load testing
- Failure testing for application replicas and Kafka

---

## Architecture

```mermaid
flowchart TB

    Client[Client]

    LB[Nginx Load Balancer]

    App1[Spring Boot Replica 1]
    App2[Spring Boot Replica 2]

    Redis[(Redis)]
    Postgres[(PostgreSQL)]

    Kafka[(Kafka)]

    URLConsumer[URL Event Consumer]
    ClickConsumer[Click Analytics Consumers x4]

    Analytics[(Click Analytics DB)]

    Client --> LB

    LB --> App1
    LB --> App2

    App1 --> Redis
    App2 --> Redis

    App1 --> Postgres
    App2 --> Postgres

    App1 --> Kafka
    App2 --> Kafka

    Kafka --> URLConsumer
    Kafka --> ClickConsumer

    ClickConsumer --> Analytics