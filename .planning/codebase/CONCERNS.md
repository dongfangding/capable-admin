# Codebase Concerns

**Analysis Date:** 2026-04-08

## Tech Debt

**Hardcoded Credentials in Configuration Files:**
- Issue: Database and Redis passwords appear in multiple YAML files
- Files: `src/main/resources/application-dev.yml` (line 11), `src/main/resources/application-local.yml` (line 8), `src/main/resources/application-pro.yml` (line 9, 39)
- Impact: Credentials exposed in version control history
- Fix approach: Use environment variables or a secrets manager (e.g., Nacos configuration with encrypted values)

**Plaintext Secrets in Production Config:**
- Issue: Production configuration contains plaintext RSA private key
- File: `src/main/resources/application-pro.yml` (lines 133-134)
- Impact: Private key for token signing exposed
- Fix approach: Move to secure vault or encrypted configuration

**S3 Configuration with Placeholder Secrets:**
- Issue: S3 access-key and secret-key are "xxx" placeholder values
- Files: `src/main/resources/application-dev.yml` (lines 21-22), `src/main/resources/application-local.yml` (lines 21-22)
- Impact: S3 integration appears incomplete or mock configuration
- Fix approach: Use proper credential management

**AES Secret Hardcoded:**
- Issue: `aesSecret: 1111111111111111` in production config
- File: `src/main/resources/application-pro.yml` (line 132)
- Impact: Weak cryptographic secret hardcoded in source
- Fix approach: Use secure key management

**Unused Import:**
- Issue: `javax.swing.Spring` imported in `PrettyAdminSecurityUtils.java` (line 11)
- File: `src/main/java/com/ddf/boot/capableadmin/infra/util/PrettyAdminSecurityUtils.java`
- Impact: Unused import, potential confusion
- Fix approach: Remove the import

**Commented Code:**
- Issue: Spring Security dependency commented out in pom.xml
- File: `pom.xml` (lines 26-29)
- Impact: Unclear if Spring Security was intentionally removed or forgotten
- Fix approach: Either remove completely or uncomment if needed

**Dead Code in AuthApplicationService:**
- Issue: `saveOnlineUser` method (lines 118-129) constructs user info but appears to do nothing with it
- File: `src/main/java/com/ddf/boot/capableadmin/application/AuthApplicationService.java`
- Impact: Incomplete implementation, code that looks like it should store online user info
- Fix approach: Implement or remove

---

## Security Considerations

**CORS AllowedOriginPatterns Wildcard:**
- Issue: `allowedOriginPatterns("*")` allows any origin
- File: `src/main/java/com/ddf/boot/capableadmin/infra/config/PrettyAdminWebConfig.java` (line 43)
- Impact: Cross-origin requests from any website are accepted
- Fix approach: Restrict to specific trusted domains

**No Rate Limiting on Auth Endpoints:**
- Issue: No mention of rate limiting on login endpoint
- File: `src/main/java/com/ddf/boot/capableadmin/controller/auth/AuthController.java`
- Impact: Brute force attacks on login are possible
- Fix approach: Add rate limiting via Sa-Token or a filter

**Token Timeout Set to 1 Hour:**
- Issue: `timeout: 3600` (1 hour) for Sa-Token
- File: `src/main/resources/application-pro.yml` (line 104)
- Impact: Short session duration, frequent re-authentication
- Fix approach: Consider longer timeout with refresh token mechanism

**Password Field Exposed in UserDetails:**
- Issue: `PrettyAdminUserDetails.password` has `@JsonIgnore` but is still populated in memory
- File: `src/main/java/com/ddf/boot/capableadmin/model/dto/PrettyAdminUserDetails.java` (line 29)
- Impact: Password in memory could be leaked via debugging or memory dumps
- Fix approach: Consider not loading password field at all after authentication

**Druid Monitor Enabled in Production:**
- Issue: `stat-view-servlet.enabled: true` with `allow: ` (empty, meaning deny all)
- File: `src/main/resources/application-pro.yml` (lines 68-73)
- Impact: Druid console access configured but empty allow list could lock out legitimate users
- Fix approach: Ensure proper IP whitelist configuration

**No Input Validation on Search Parameters:**
- Issue: SQL `LIKE` queries without sanitization in mapper XML
- File: `src/main/resources/mapper/ext/SysMenuExtMapper.xml` (lines 64-67)
- Impact: Potential for SQL injection if `keyword` parameter contains special characters
- Fix approach: Add input sanitization or use parameterized wildcards

**MyBatis `${}` Syntax Used:**
- Issue: `${@com.ddf.boot.capableadmin.enums.MenuTypeEnum@BUTTON.getValue()}` in mapper XML
- File: `src/main/resources/mapper/ext/SysMenuExtMapper.xml` (lines 94, 104)
- Impact: Although this references an enum value, `${}` is generally risky
- Fix approach: Use `#{}` with proper parameter passing if values are dynamic

---

## Performance Considerations

**Thread Pool Not Monitored:**
- Issue: `CapableAdminThreadConfig.globalExecutor()` creates a thread pool but is not included in observability monitoring
- File: `src/main/java/com/ddf/boot/capableadmin/infra/config/CapableAdminThreadConfig.java`
- Impact: Thread pool metrics not exposed to Prometheus
- Fix approach: Add `@ManagedExecutor` annotation or include in observability scan

**N+1 Query Potential in Role Loading:**
- Issue: `loadUserDetail` method queries roles then potentially queries permissions
- File: `src/main/java/com/ddf/boot/capableadmin/service/PrettyAdminUserDetailsService.java` (lines 54-82)
- Impact: Multiple database roundtrips per user login
- Fix approach: Consider JOIN queries or batch loading

**Redis Key Pattern Uses toString:**
- Issue: `getUserLoginDetails` uses `userId.toString()` for Redis key
- File: `src/main/java/com/ddf/boot/capableadmin/infra/repository/SysUserRepository.java` (line 50)
- Impact: Potential key mismatch if type conversion varies
- Fix approach: Use explicit string conversion

**No Connection Pooling Configuration for Redis:**
- Issue: Redis configuration relies on defaults from `ddf-common-redisson`
- File: `src/main/resources/application-pro.yml` (line 33)
- Impact: Connection pool settings not explicitly tuned
- Fix approach: Add explicit Redisson connection pool configuration

---

## Operational Concerns

**Monitoring Endpoint Path Changed:**
- Issue: `base-path: /monitor` instead of default `/actuator`
- File: `src/main/resources/application-pro.yml` (line 169)
- Impact: Non-standard path may confuse monitoring tools
- Fix approach: Document the change or consider using standard paths with network restrictions

**Health Check Shows Details When Authorized:**
- Issue: `show-details: when_authorized` with `roles: "ROLE_MONITOR"`
- File: `src/main/resources/application-pro.yml` (lines 171-175)
- Impact: Requires manual role configuration for monitoring
- Fix approach: Ensure ROLE_MONITOR role exists in database

**Graceful Shutdown Timeout 120s:**
- Issue: `timeout-per-shutdown-phase: 120s` is very long
- File: `src/main/resources/application-pro.yml` (line 21)
- Impact: Slow draining of requests during deployment
- Fix approach: Consider shorter timeout (30-60s)

**Tomcat Max Connections 10000:**
- Issue: High `max-connections` with relatively low thread count
- File: `src/main/resources/application-pro.yml` (lines 2-8)
- Impact: Many connections may queue with 100 acceptCount limit
- Fix approach: Balance connection pool and thread counts properly

---

## Code Quality Observations

**Large Service Class:**
- Issue: `SysMenuService` is 400 lines
- File: `src/main/java/com/ddf/boot/capableadmin/service/SysMenuService.java`
- Impact: Hard to maintain, too many responsibilities
- Fix approach: Consider splitting into smaller services by domain

**Inconsistent Exception Handling:**
- Issue: Some places throw `BusinessException` with string message, others with enum code
- Files: `src/main/java/com/ddf/boot/capableadmin/service/SysRoleService.java` (line 88), `src/main/java/com/ddf/boot/capableadmin/service/SysJobService.java` (lines 71, 76)
- Impact: Inconsistent error responses
- Fix approach: Standardize on enum codes only

**Magic Number:**
- Issue: `Long.valueOf(0L)` used to check if pid is root
- File: `src/main/java/com/ddf/boot/capableadmin/service/SysMenuService.java` (lines 87-88)
- Impact: Unclear meaning
- Fix approach: Extract to named constant

**Commented Code Block:**
- Issue: `PrettyAdminSecurityUtils.getCurrentUser()` has commented out line
- File: `src/main/java/com/ddf/boot/capableadmin/infra/util/PrettyAdminSecurityUtils.java` (line 48)
- Impact: Dead code cluttering codebase
- Fix approach: Remove commented code

---

## Test Coverage Gaps

**No Integration Tests for Repository Layer:**
- Issue: No test files for `SysUserRepository`, `SysMenuRepository`
- Impact: Data access layer not validated against real database
- Fix approach: Add Testcontainers-based integration tests

**Limited Controller Tests:**
- Issue: Only basic controller tests exist for some controllers
- Files: `src/test/java/com/ddf/boot/capableadmin/controller/sys/*`
- Impact: API contract not fully validated
- Fix approach: Add parameterized tests for various input scenarios

**No Security Tests:**
- Issue: No tests for authentication/authorization flows
- Impact: Security regressions may go undetected
- Fix approach: Add tests for login, logout, token expiration, permission checks

**No Performance Tests:**
- Issue: No load or stress tests
- Impact: Performance bottlenecks not discovered until production
- Fix approach: Add JMeter or Gatling-based performance tests

**Missing Test for Cache Manager:**
- Issue: `PrettyAdminCacheManager` has no test file
- File: `src/main/java/com/ddf/boot/capableadmin/service/PrettyAdminCacheManager.java`
- Impact: Cache behavior not validated
- Fix approach: Add unit tests with mocked Redis

---

## Secrets Management

**Hardcoded in YAML Files:**
- Issue: MySQL password `ItIsSnowball` in multiple config files
- Files: `application-dev.yml`, `application-local.yml`, `application-pro.yml`
- Impact: Secrets in version control
- Fix approach: Use environment variables or Nacos encrypted config

**Email Credentials Hardcoded:**
- Issue: QQ email credentials in config
- Files: `application-dev.yml`, `application-local.yml`
- Impact: Email service credentials exposed
- Fix approach: Use environment variables

**Druid Console Password:**
- Issue: `login-password: Aa&123456` hardcoded
- File: `src/main/resources/application-pro.yml` (line 73)
- Impact: Database monitoring console accessible with default password
- Fix approach: Change to strong unique password via environment variable

---

## Missing Critical Features

**No Token Refresh Mechanism:**
- Issue: Users must re-login after token expires (1 hour)
- File: `src/main/resources/application-pro.yml` (line 104)
- Impact: Poor user experience
- Fix approach: Implement refresh token functionality

**No Password Complexity Validation:**
- Issue: No validation when creating or resetting passwords
- Files: `src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysUserCreateRequest.java`
- Impact: Weak passwords can be set
- Fix approach: Add password strength validation

**No Audit Log Retention Policy:**
- Issue: `SysLog` table grows indefinitely
- File: `src/main/java/com/ddf/boot/capableadmin/model/entity/SysLog.java`
- Impact: Database storage bloat
- Fix approach: Implement log rotation or archival

**No User Session Management:**
- Issue: No way to view or invalidate active sessions
- File: `src/main/java/com/ddf/boot/capableadmin/application/AuthApplicationService.java`
- Impact: Cannot force logout or see who's logged in
- Fix approach: Add session list/invalidate endpoint

---

*Concerns audit: 2026-04-08*
