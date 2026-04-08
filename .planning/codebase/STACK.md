# Technology Stack

**Analysis Date:** 2026-04-08

## Languages

**Primary:**
- Java 17 - All backend services

## Runtime

**Environment:**
- JDK 17 (Eclipse Temurin JRE, alpine/jammy base image)
- JVM: ZGC garbage collector (`-XX:+UseZGC`)
- Container-ready with `+UseContainerSupport`

**Build Tool:**
- Maven (parent pom: `ddf-common` - `boot3.5-2026.1-SNAPSHOT`)

## Frameworks

**Core:**
- Spring Boot 3.5.x - Application framework
- Spring MVC - Web layer

**Testing:**
- Spring Boot Test - Testing framework

**Build/Dev:**
- Spring Boot Maven Plugin - JAR packaging

## Key Dependencies

**Core Framework:**
- `ddf-common-starter-default` - Internal common library
- `ddf-common-captcha` - Captcha functionality
- `ddf-common-redis` - Redis integration

**Authentication/Authorization:**
- `sa-token-spring-boot3-starter` 1.44.0 - Authentication framework (StampedLock-based)
- `sa-token-redisson-spring-boot-starter` 1.44.0 - Redis-backed session storage

**Data Access:**
- MyBatis - ORM framework
- Druid - Connection pool (com.alibaba.druid.pool.DruidDataSource)
- MySQL 8.x driver (`com.mysql.cj.jdbc.Driver`)

**Cache:**
- Redisson - Redis client (configured via `redisson.yml`)
- Redis 6.x compatible

**Object Storage:**
- `ddf-common-s3` - S3-compatible storage (MinIO/七牛云/Qiniu)

**IP Geolocation:**
- `mica-ip2region` 3.5.7 - IP to region mapping

**Observability:**
- `opentelemetry-api` 1.59.0 - Distributed tracing API
- OpenTelemetry Java Agent 2.25.0 - Auto-instrumentation
- Micrometer + Prometheus - Metrics export

**Utilities:**
- Lombok - Code generation

**Rate Limiting & Repeatable Requests:**
- `ddf-common` rate limit annotations
- Redis-based repeatable submit validation

**Scheduling:**
- Spring Async (`@EnableAsync`)
- Spring Scheduling (`@EnableScheduling`)

**Logging:**
- Log4j 2.x (async logging via `AsyncLoggerContextSelector`)
- Log4j2 XML configuration at `src/main/resources/log4j2.xml`

## Configuration

**Application Configuration:**
- Primary: `src/main/resources/application.yml`
- Environment-specific: `application-dev.yml`, `application-local.yml`, `application-pro.yml`
- Profile activation: `spring.profiles.active`

**External Configuration (Nacos):**
- `nacos:common-redis.yaml` - Redis shared config
- `nacos:common-datasource.yaml` - Database shared config
- `nacos:common-server.yaml` - Server shared config
- `nacos:mybatis.yaml` - MyBatis shared config
- `nacos:comm-rocketmq.yaml` - RocketMQ shared config
- `nacos:openfeign.yaml` - OpenFeign shared config

**Environment Variables:**
- `PROFILE` - Active Spring profile (default: local)
- `HeapSize` - JVM heap size (default: 512m)
- `MetaspaceSize` - JVM metaspace size (default: 256m)
- `OLTP_URL` - OpenTelemetry collector endpoint

## Platform Requirements

**Development:**
- Java 17+
- Maven 3.x
- MySQL 8.x
- Redis 6.x
- Nacos (for configuration management)

**Production:**
- Docker container (eclipse-temurin:17-jre-jammy)
- MySQL 8.x
- Redis 6.x
- OpenTelemetry Collector (OTLP gRPC, port 4317)

---

*Stack analysis: 2026-04-08*
