# Testing Patterns

**Analysis Date:** 2026-04-08

## Test Framework

**Primary Framework:**
- JUnit 5 (`org.junit.jupiter.api`) - Test runner and assertions

**Mocking:**
- Mockito (`org.mockito.Mockito`, `org.mockito.MockedStatic`) - Mocking framework

**Spring Boot Testing:**
- `spring-boot-starter-test` - Includes JUnit 5, Mockito, AssertJ

**Mocking Static Methods:**
```java
import org.mockito.Mockito;
import org.mockito.MockedStatic;

try (MockedStatic<PrettyAdminSecurityUtils> mockedStatic = Mockito.mockStatic(PrettyAdminSecurityUtils.class)) {
    mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUser).thenReturn(userDetails);
    // test code
}
```

## Test File Organization

**Location:**
- Mirror `src/main/java` structure in `src/test/java`
- `src/test/java/com/ddf/boot/capableadmin/`

**Directory Structure:**
```
src/test/java/com/ddf/boot/capableadmin/
├── application/           # Application service tests
│   ├── SysUserApplicationServiceTest.java
│   ├── SysLogApplicationServiceTest.java
│   └── SysDictApplicationServiceTest.java
├── controller/            # Controller tests
│   ├── sys/
│   │   ├── UserControllerTest.java
│   │   ├── SysLogControllerTest.java
│   │   ├── SysDictControllerTest.java
│   │   └── SysDictDetailControllerTest.java
│   └── auth/
│       └── AuthControllerTest.java
├── infra/                 # Infrastructure tests
│   ├── audit/
│   │   └── AdminAuditLogAspectTest.java
│   ├── config/
│   │   └── StpInterfaceImplTest.java
│   └── util/
│       ├── PrettyAdminSecurityUtilsTest.java
│       └── CapableAdminUtilsTest.java
└── service/               # Service tests
    └── AdminAuditLogServiceTest.java
```

**Naming Convention:**
- Test class: `{ClassName}Test.java`
- Test method: `should{Behavior}()` or `{methodName}Should{Behavior}()`

## Test Structure Patterns

### Controller Test Pattern
```java
package com.ddf.boot.capableadmin.controller.sys;

class UserControllerTest {

    @Test
    void profileShouldReturnCurrentUserProfile() {
        SysUserApplicationService applicationService = Mockito.mock(SysUserApplicationService.class);
        UserController controller = new UserController(applicationService);
        try (MockedStatic<PrettyAdminSecurityUtils> mockedStatic = Mockito.mockStatic(PrettyAdminSecurityUtils.class)) {
            PrettyAdminUserDetails userDetails = buildDetails();
            mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUser).thenReturn(userDetails);
            Mockito.when(applicationService.getCurrentUserProfile(1L)).thenReturn(buildProfileResponse());

            UserProfileResponse response = controller.profile().getData();

            Assertions.assertEquals("tester", response.getUsername());
            Assertions.assertEquals("tester@example.com", response.getEmail());
        }
    }

    private PrettyAdminUserDetails buildDetails() {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUsername("tester");
        user.setNickname("tester");
        user.setPassword("pwd");
        user.setEnabled(Boolean.TRUE);
        return new PrettyAdminUserDetails(user, Set.of("admin"), Set.of("System:User:Edit"), 1, false);
    }

    private UserProfileResponse buildProfileResponse() {
        UserProfileResponse response = new UserProfileResponse();
        response.setUserId(1L);
        response.setUsername("tester");
        response.setEmail("tester@example.com");
        // ... set other fields
        return response;
    }
}
```

### Application Service Test Pattern
```java
package com.ddf.boot.capableadmin.application;

class SysUserApplicationServiceTest {

    @Test
    void updateCurrentUserProfileShouldClearCache() {
        SysUserMapper sysUserMapper = Mockito.mock(SysUserMapper.class);
        PrettyAdminCacheManager cacheManager = Mockito.mock(PrettyAdminCacheManager.class);
        SysUserApplicationService service = new SysUserApplicationService(
                Mockito.mock(SysDeptMapper.class),
                Mockito.mock(SysDeptService.class),
                Mockito.mock(SysUserDeptMapper.class),
                sysUserMapper,
                Mockito.mock(SysUserJobMapper.class),
                Mockito.mock(SysUserRoleMapper.class),
                new BCryptPasswordEncoder(),
                cacheManager
        );
        SysUser sysUser = new SysUser();
        sysUser.setUserId(1L);
        sysUser.setUsername("tester");
        sysUser.setNickname("old");
        sysUser.setEnabled(Boolean.TRUE);
        Mockito.when(sysUserMapper.selectByPrimaryKey(1L)).thenReturn(sysUser);
        Mockito.when(sysUserMapper.selectByEmail("new@example.com")).thenReturn(null);
        Mockito.when(sysUserMapper.selectByMobile("13800000000")).thenReturn(null);

        UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        request.setNickname("new");
        request.setEmail("new@example.com");
        request.setMobile("13800000000");
        // ... set other fields

        service.updateCurrentUserProfile(1L, request);

        Mockito.verify(cacheManager).cleanUserAllCache(1L);
        Mockito.verify(sysUserMapper).updateByPrimaryKeySelective(sysUser);
    }

    @Test
    void changeCurrentUserPasswordShouldRejectWhenOldPasswordDoesNotMatch() {
        // ... setup with wrong password
        Assertions.assertThrows(BusinessException.class, () -> service.changeCurrentUserPassword(2L, request));
        Mockito.verify(cacheManager, Mockito.never()).cleanUserAllCache(2L);
    }
}
```

### Service Test Pattern (No Spring Context)
```java
package com.ddf.boot.capableadmin.service;

class AdminAuditLogServiceTest {

    @Test
    void shouldMaskPasswordFieldsAndTruncateOversizedParams() {
        SysLogMapper sysLogMapper = Mockito.mock(SysLogMapper.class);
        AdminAuditLogService service = new AdminAuditLogService(sysLogMapper);
        SysLog log = new SysLog();
        log.setParams("{\"password\":\"abc\",\"payload\":\"" + "x".repeat(5000) + "\"}");

        service.save(log);

        ArgumentCaptor<SysLog> captor = ArgumentCaptor.forClass(SysLog.class);
        Mockito.verify(sysLogMapper).insertSelective(captor.capture());
        String params = captor.getValue().getParams();
        Assertions.assertFalse(params.contains("abc"));
        Assertions.assertTrue(params.contains("***"));
        Assertions.assertTrue(params.length() <= 2048);
    }
}
```

### Aspect Test Pattern
```java
package com.ddf.boot.capapbleadmin.infra.audit;

class AdminAuditLogAspectTest {

    @Test
    void shouldWriteSuccessLogForAnnotatedMethod() throws Throwable {
        AdminAuditLogService auditLogService = Mockito.mock(AdminAuditLogService.class);
        AdminAuditLogAspect aspect = new AdminAuditLogAspect(auditLogService);
        ProceedingJoinPoint joinPoint = mockJoinPoint("ok");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/sys-user/reset-password");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Object result = aspect.around(joinPoint, annotatedMethod().getAnnotation(AdminAuditLog.class));

        Assertions.assertEquals("ok", result);
        ArgumentCaptor<SysLog> captor = ArgumentCaptor.forClass(SysLog.class);
        Mockito.verify(auditLogService).save(captor.capture());
        Assertions.assertEquals("SUCCESS", captor.getValue().getLogType());
    }

    @Test
    void shouldWriteFailureLogAndRethrowOriginalException() throws Throwable {
        // ... similar setup
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> aspect.around(joinPoint, annotatedMethod().getAnnotation(AdminAuditLog.class))
        );
        Assertions.assertEquals("boom", exception.getMessage());
        // verify FAIL log was saved
    }
}
```

## Mocking Patterns

**Mocking Regular Dependencies:**
```java
SysUserMapper sysUserMapper = Mockito.mock(SysUserMapper.class);
Mockito.when(sysUserMapper.selectByPrimaryKey(1L)).thenReturn(sysUser);
Mockito.verify(sysUserMapper).updateByPrimaryKeySelective(sysUser);
```

**Mocking Static Utilities:**
```java
try (MockedStatic<PrettyAdminSecurityUtils> mockedStatic = Mockito.mockStatic(PrettyAdminSecurityUtils.class)) {
    mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUser).thenReturn(userDetails);
    mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUserId).thenReturn(1L);
    // test code
}
```

**Mocking Constructed Objects:**
```java
new BCryptPasswordEncoder()  // Real object for password encoding tests
```

**Verifying No Interaction:**
```java
Mockito.verify(cacheManager, Mockito.never()).cleanUserAllCache(2L);
```

**InOrder Verification:**
```java
InOrder inOrder = Mockito.inOrder(sysDictDetailExtMapper, sysDictMapper);
inOrder.verify(sysDictDetailExtMapper).deleteByDictId(1L);
inOrder.verify(sysDictMapper).deleteByPrimaryKey(1L);
```

**Argument Capture:**
```java
ArgumentCaptor<SysLog> captor = ArgumentCaptor.forClass(SysLog.class);
Mockito.verify(sysLogMapper).insertSelective(captor.capture());
String params = captor.getValue().getParams();
```

## Test Fixtures

**Building Test Entities:**
```java
private PrettyAdminUserDetails buildDetails(Long userId, Set<String> permissions, Set<String> roles) {
    SysUser sysUser = new SysUser();
    sysUser.setUserId(userId);
    sysUser.setUsername("tester");
    sysUser.setNickname("tester");
    sysUser.setPassword("pwd");
    sysUser.setEnabled(Boolean.TRUE);
    return new PrettyAdminUserDetails(sysUser, roles, permissions, 1, false);
}
```

## Assertions

**Standard JUnit Assertions:**
```java
Assertions.assertEquals("tester", response.getUsername());
Assertions.assertTrue(codes.contains("System:Menu:List"));
Assertions.assertFalse(params.contains("abc"));
Assertions.assertTrue(params.length() <= 2048);
Assertions.assertNotNull(method.getAnnotation(AdminAuditLog.class));
```

**Exception Testing:**
```java
Assertions.assertThrows(BusinessException.class, () -> service.changeCurrentUserPassword(2L, request));
```

## Test Coverage Areas

**Unit Tests:**
- `AdminAuditLogService` - Parameter sanitization logic
- `SysUserApplicationService` - Business logic, cache interaction
- `SysLogApplicationService` - Delete operations

**Controller Tests:**
- `UserController` - Profile retrieval, profile update, password change
- `AuthController` - Permission codes retrieval
- `SysDictDetailController` - List, persist, delete delegation
- `SysLogController` - List delegation
- `SysDictController` - List, persist, delete delegation

**Infrastructure Tests:**
- `AdminAuditLogAspect` - Audit logging behavior
- `StpInterfaceImpl` - Permission/role reading from user details
- `PrettyAdminSecurityUtils` - Security utility functions
- `CapableAdminUtils` - IP address utility

**Not Covered (Manual/Integration Testing):**
- MyBatis mapper XML SQL correctness
- Repository Redis operations
- End-to-end API flows

## Test Run Commands

**Run all tests:**
```bash
mvn test
```

**Run specific test class:**
```bash
mvn test -Dtest=UserControllerTest
```

**Run with Maven:**
```bash
./mvnw test
```

## Key Observations

**Strengths:**
- Tests are pure unit tests (no Spring context required)
- Static utility mocking with `MockedStatic` works correctly
- Argument captors used appropriately for log verification
- Constructor injection makes mocking straightforward

**Patterns to Follow:**
- Create real objects for simple value types rather than mocking
- Use `Mockito.never()` to verify methods were NOT called
- Use `InOrder` to verify sequence of operations
- Use `try-with-resources` for `MockedStatic` to ensure cleanup
- Build test fixtures as helper methods in each test class

---

*Testing analysis: 2026-04-08*
