# Admin Audit Log Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Add annotation-driven operation audit logging for admin write endpoints and persist entries into `sys_log` without changing existing table structure.

**Architecture:** Introduce a lightweight `@AdminAuditLog` annotation on selected controller methods and capture those operations through an AOP aspect. The aspect collects request context, operator identity, sanitized parameters, execution time, and exception state, then delegates persistence to a dedicated service that writes through `SysLogMapper`.

**Tech Stack:** Spring Boot 3, Spring AOP, MyBatis, Sa-Token, Maven, JUnit 5 / Mockito

---

### Task 1: Add failing tests for annotation-driven audit logging

**Files:**
- Create: `src/test/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogAspectTest.java`
- Create: `src/test/java/com/ddf/boot/capableadmin/service/AdminAuditLogServiceTest.java`
- Modify: `src/test/java/com/ddf/boot/capableadmin/controller/sys/UserControllerTest.java`

**Step 1: Write the failing success-case aspect test**

```java
@Test
void shouldWriteSuccessLogForAnnotatedMethod() {
    // arrange annotated target, mock request context, invoke aspect, verify SysLogMapper called
}
```

**Step 2: Run test to verify it fails**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=AdminAuditLogAspectTest" test`
Expected: FAIL because annotation/aspect/service do not exist yet

**Step 3: Write the failing failure-case aspect test**

```java
@Test
void shouldWriteFailureLogAndRethrowOriginalException() {
    // annotated method throws, aspect writes failure log, exception still escapes
}
```

**Step 4: Run test to verify it fails**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=AdminAuditLogAspectTest" test`
Expected: FAIL

**Step 5: Write the failing sanitizer test**

```java
@Test
void shouldMaskPasswordFieldsAndTruncateOversizedParams() {
    // verify password fields are masked and long payload is truncated
}
```

**Step 6: Run test to verify it fails**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=AdminAuditLogServiceTest" test`
Expected: FAIL

**Step 7: Commit**

```bash
git add src/test/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogAspectTest.java src/test/java/com/ddf/boot/capableadmin/service/AdminAuditLogServiceTest.java src/test/java/com/ddf/boot/capableadmin/controller/sys/UserControllerTest.java
git commit -m "test: add audit log scaffolding"
```

### Task 2: Implement annotation, sanitizer, and persistence service

**Files:**
- Create: `src/main/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLog.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogPayloadSanitizer.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/service/AdminAuditLogService.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/model/entity/SysLog.java` only if a helper field is strictly necessary; otherwise leave it unchanged
- Test: `src/test/java/com/ddf/boot/capableadmin/service/AdminAuditLogServiceTest.java`

**Step 1: Add the annotation**

```java
public @interface AdminAuditLog {
    String module();
    String action();
}
```

**Step 2: Add parameter sanitization**

```java
public String sanitize(Object[] args) {
    // remove request/response/files, mask password-like keys, truncate
}
```

**Step 3: Add audit persistence service**

```java
public void saveSuccess(...);
public void saveFailure(...);
```

**Step 4: Run targeted tests**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=AdminAuditLogServiceTest" test`
Expected: PASS

**Step 5: Commit**

```bash
git add src/main/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLog.java src/main/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogPayloadSanitizer.java src/main/java/com/ddf/boot/capableadmin/service/AdminAuditLogService.java src/test/java/com/ddf/boot/capableadmin/service/AdminAuditLogServiceTest.java
git commit -m "feat: add audit log annotation and persistence service"
```

### Task 3: Implement the audit aspect

**Files:**
- Create: `src/main/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogAspect.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/CapableAdminApplication.java` only if AOP scanning is not already active
- Test: `src/test/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogAspectTest.java`

**Step 1: Implement around advice**

```java
@Around("@annotation(adminAuditLog)")
public Object around(ProceedingJoinPoint joinPoint, AdminAuditLog adminAuditLog) throws Throwable {
    long start = System.currentTimeMillis();
    try {
        Object result = joinPoint.proceed();
        auditLogService.saveSuccess(...);
        return result;
    } catch (Throwable ex) {
        auditLogService.saveFailure(...);
        throw ex;
    }
}
```

**Step 2: Collect request context safely**

```java
HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
```

**Step 3: Make logging failure non-blocking**

```java
try {
    sysLogMapper.insertSelective(log);
} catch (Exception e) {
    log.error("save audit log failed", e);
}
```

**Step 4: Run targeted tests**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=AdminAuditLogAspectTest" test`
Expected: PASS

**Step 5: Commit**

```bash
git add src/main/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogAspect.java src/test/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogAspectTest.java src/main/java/com/ddf/boot/capableadmin/CapableAdminApplication.java
git commit -m "feat: add audit log aspect"
```

### Task 4: Annotate first-batch controller write endpoints

**Files:**
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/UserController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysUserController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysRoleController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysMenuController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDeptController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysJobController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDictController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDictDetailController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysLogController.java`
- Test: `src/test/java/com/ddf/boot/capableadmin/controller/sys/UserControllerTest.java`

**Step 1: Add `@AdminAuditLog` to user profile/password write methods**

```java
@AdminAuditLog(module = "个人中心", action = "修改个人资料")
@PostMapping("profile")
```

**Step 2: Add `@AdminAuditLog` to system write methods**

Examples:

```java
@AdminAuditLog(module = "用户管理", action = "重置密码")
@PostMapping("reset-password")
```

```java
@AdminAuditLog(module = "角色管理", action = "更新角色菜单")
@PostMapping("update-role-menu")
```

**Step 3: Keep query methods unannotated**

Do not annotate:
- `list`
- `query`
- `profile` read endpoint
- `sys-log/list`

**Step 4: Run focused controller tests**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=UserControllerTest" test`
Expected: PASS

**Step 5: Commit**

```bash
git add src/main/java/com/ddf/boot/capableadmin/controller/sys/UserController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysUserController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysRoleController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysMenuController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDeptController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysJobController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDictController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDictDetailController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysLogController.java
git commit -m "feat: annotate admin write operations for audit"
```

### Task 5: Add end-to-end verification for one annotated controller path

**Files:**
- Create: `src/test/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogIntegrationTest.java`
- Modify: `src/test/java/com/ddf/boot/capableadmin/controller/sys/UserControllerTest.java` only if simpler coverage is enough in this repo

**Step 1: Write a failing verification test**

```java
@Test
void shouldPersistAuditLogWhenAnnotatedControllerMethodRuns() {
    // invoke annotated method and verify SysLogMapper.insertSelective called
}
```

**Step 2: Run test to verify it fails**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=AdminAuditLogIntegrationTest" test`
Expected: FAIL until wiring is complete

**Step 3: Implement the minimal missing glue**

Potential fixes:
- request context mock
- security username mock
- bean wiring

**Step 4: Run targeted test**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=AdminAuditLogIntegrationTest" test`
Expected: PASS

**Step 5: Commit**

```bash
git add src/test/java/com/ddf/boot/capableadmin/infra/audit/AdminAuditLogIntegrationTest.java src/test/java/com/ddf/boot/capableadmin/controller/sys/UserControllerTest.java
git commit -m "test: verify annotated audit logging flow"
```

### Task 6: Final verification

**Files:**
- Test: `src/test/java/com/ddf/boot/capableadmin/infra/audit/`
- Test: `src/test/java/com/ddf/boot/capableadmin/controller/sys/`
- Test: `src/test/java/com/ddf/boot/capableadmin/service/`

**Step 1: Run audit-focused tests**

Run: `mvn -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml "-Dmaven.test.skip=false" "-DskipTests=false" "-Dtest=AdminAuditLogAspectTest,AdminAuditLogServiceTest,AdminAuditLogIntegrationTest,UserControllerTest" test`
Expected: PASS

**Step 2: Run compile verification**

Run: `mvn -q -s D:\develop_tools\apache-maven-3.9.9\conf\settings-snowball.xml -DskipTests compile`
Expected: BUILD SUCCESS

**Step 3: Manual verification**

1. Call `POST /sys-user/reset-password`
2. Call `POST /user/change-password`
3. Call `POST /sys-dict/persist`
4. Query `GET /sys-log/list`

Expected:
- success calls create success logs
- forced failures create failure logs
- masked password fields do not appear in plaintext

**Step 4: Commit**

```bash
git add .
git commit -m "feat: add admin operation audit logging"
```
