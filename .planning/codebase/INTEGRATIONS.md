# External Integrations

**Analysis Date:** 2026-04-08

## Service Discovery & Configuration

**Nacos (Alibaba)**
- Discovery namespace: `d473baa9-7b63-4b9e-bd6b-03d411ea28e2`
- Config group: `KVITA_GROUP` / `JAVA_GROUP`
- Used for centralized configuration management
- Profiles: `common-redis.yaml`, `common-datasource.yaml`, `common-server.yaml`, `mybatis.yaml`, `comm-rocketmq.yaml`, `openfeign.yaml`
- Auth via env vars: `NACOS_USERNAME`, `NACOS_PASSWORD`, `NACOS_SERVER`, `NACOS_KEY`

## Data Storage

**MySQL**
- Driver: `com.mysql.cj.jdbc.Driver`
- Connection pool: Druid (max-active: 200, min-idle: 5)
- Database name: `capable_admin`
- Default host: `10.88.1.23:3306`
- Charset: utf8mb4
- Connection properties: `useSSL=false`, `allowPublicKeyRetrieval=true`, `serverTimezone=Asia/Shanghai`
- SQL logs: enabled (slow-sql threshold: 3000ms)

**Redis**
- Client: Redisson
- Config file: `classpath:redisson.yml`
- Default host: `10.88.1.23:6379`
- Database: 1
- Used for: sessions (Sa-Token), caching, rate limiting, repeatable request validation

**S3-Compatible Storage**
- Provider: MinIO or similar S3-compatible storage
- Config: `customizer.infra.s3.*`
- Endpoint: `http://10.88.1.23:9000` (dev/local)
- Region: `cn-bj1`
- Used for: file uploads and object storage

## Object Storage (Cloud)

**七牛云 (Qiniu Cloud)**
- SDK: Qiniu Cloud Java SDK
- Entities: `ToolQiniuConfig`, `ToolQiniuContent`
- Mapper: `ToolQiniuConfigMapper`, `ToolQiniuContentMapper`
- Features: bucket management, content storage, CDN host configuration
- Tables: `tool_qiniu_config`, `tool_qiniu_content`

## Authentication & Authorization

**Sa-Token (StampedLock-based)**
- Framework: `sa-token-spring-boot3-starter` 1.44.0
- Session storage: Redis (via `sa-token-redisson-spring-boot-starter`)
- Token config:
  - Name: `access-token`
  - Timeout: 3600 seconds (1 hour)
  - Active timeout: -1 (never expires)
  - Concurrent login: disabled
  - Token style: UUID
  - Cookie read: disabled
- Permission loading: `StpInterfaceImpl` (custom implementation)
- Custom user details: `PrettyAdminUserDetailsService`

## IM Integration

**融云 (RongCloud)**
- SDK: `RongIMLib-5.9.5.prod.js`
- Client-side: `src/main/resources/static/rongcloud-im.js`
- Demo page: `src/main/resources/static/rongcloud-im-demo.html`
- Features: IM chat, chatroom management, message handling
- AppKey configured in demo: `4z3hlwrv4dqnt`

## RPC & HTTP Clients

**OpenFeign**
- Config imported from Nacos: `openfeign.yaml`
- Used for microservice-to-microservice communication

**RocketMQ**
- Config imported from Nacos: `comm-rocketmq.yaml`
- Message queue for asynchronous communication

## Email Service

**QQ Mail SMTP**
- Host: `smtp.qq.com`
- Port: 465 (SSL)
- Auth: PLAIN/LOGIN
- StartTLS: enabled (required)
- Used for: system notifications

## Observability

**OpenTelemetry**
- Agent: `opentelemetry-javaagent-2.25.0.jar`
- Protocol: OTLP gRPC
- Exporter endpoint: `${OLTP_URL}` (env var, typically `10.88.1.190:4317`)
- Service name: `capable-admin`

**Micrometer + Prometheus**
- Metrics endpoint: `/monitor/prometheus`
- Other endpoints: `/monitor/health`, `/monitor/info`, `/monitor/metrics`
- Base path modified to `/monitor` (not `/actuator`)

**Health Checks**
- Liveness probe: enabled
- Readiness probe: enabled
- DB health: enabled
- Redis health: enabled
- Mail health: disabled

## Security

**Global Exception Handler**
- `PrettyAdminExceptionHandler` - Handles Sa-Token exceptions
- NotLoginException -> 401 Unauthorized
- NotPermissionException -> 403 Forbidden

**Rate Limiting**
- `@EnableRateLimit` - Global rate limiting
- Redis-based validator

**Repeatable Request Protection**
- `@EnableRepeatable` with `RedisRepeatableValidator`
- Prevents duplicate form submissions

**Thread Pool Monitoring**
- Custom thread pool metrics enabled
- Pattern matching: `*Executor`, `*Pool`, `*Scheduler`
- Metric name: `custom.thread.pool`

## Environment Configuration

**Required env vars (for production):**
- `PROFILE` - Spring profile (local/dev/pro)
- `NACOS_USERNAME` - Nacos username
- `NACOS_PASSWORD` - Nacos password
- `NACOS_SERVER` - Nacos server address
- `OLTP_URL` - OpenTelemetry collector endpoint
- `HeapSize` - JVM heap size
- `MetaspaceSize` - JVM metaspace size

**Secrets (via application configs, not committed):**
- Database password: `ItIsSnowball` (dev only)
- Redis password: `ItIsSnowball` (dev only)
- S3 access keys
- RSA keys for encryption (public/private key pair configured in `application.yml`)

---

*Integration audit: 2026-04-08*
