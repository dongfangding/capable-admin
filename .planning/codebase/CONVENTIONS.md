# Coding Conventions

**Analysis Date:** 2026-04-08

## Languages

**Primary:**
- Java 17 - All application code

**Secondary:**
- XML - MyBatis mapper files (`src/main/resources/mapper/`)

## Project Structure

**Package Root:** `com.ddf.boot.capableadmin`

**Directory Layout:**
```
src/main/java/com/ddf/boot/capableadmin/
├── application/          # Application services (use cases)
├── controller/           # REST controllers
│   ├── auth/            # Authentication controllers
│   └── sys/             # System management controllers
├── enums/               # Enumeration types
├── infra/               # Infrastructure layer
│   ├── audit/           # Audit logging aspect and annotation
│   ├── config/          # Spring configuration classes
│   ├── extra/           # Extra/extension utilities
│   ├── mapper/          # MyBatis mapper interfaces
│   │   └── ext/         # Extended mappers with custom SQL
│   ├── repository/      # Repository pattern for cached data
│   └── util/            # Utility classes
├── model/               # Domain models
│   ├── cqrs/           # Command Query Responsibility Segregation
│   ├── dto/            # Data Transfer Objects
│   ├── entity/         # Database entities
│   ├── request/        # API request objects
│   │   ├── auth/       # Auth request objects
│   │   └── sys/        # System request objects
│   └── response/       # API response objects
│       ├── auth/
│       └── sys/
└── service/            # Domain services
```

---

## Naming Conventions

**Classes:**
- PascalCase: `SysUserController`, `AuthApplicationService`, `PrettyAdminExceptionCode`
- Entity suffix: `{EntityName}.java` - `SysUser.java`, `SysDept.java`
- Request suffix: `{Entity}{Action}Request.java` - `SysUserCreateRequest.java`, `UserChangePasswordRequest.java`
- Response suffix: `{Entity}{Purpose}Response.java` - `SysUserRes.java`, `UserProfileResponse.java`
- Service suffix: `{Domain}Service.java` - `AdminAuditLogService.java`
- Application Service suffix: `{Domain}ApplicationService.java` - `SysUserApplicationService.java`
- Mapper suffix: `{Entity}Mapper.java` - `SysUserMapper.java`
- Controller suffix: `{Entity}Controller.java` - `UserController.java`, `AuthController.java`

**Methods:**
- camelCase: `getCurrentUserId()`, `updateCurrentUserProfile()`, `changePassword()`
- Query methods: `selectBy{Field}()` - `selectByUsername()`, `selectByEmail()`
- List methods: `listAll()`, `queryAll()`
- Save methods: `save()`, `persistUser()`, `insertSelective()`
- Update methods: `updateByPrimaryKeySelective()`
- Delete methods: `deleteByPrimaryKey()`, `deleteByUserId()`

**Variables:**
- camelCase: `sysUser`, `userId`, `passwordEncoder`
- Constants: SCREAMING_SNAKE_CASE - `MAX_PARAMS_LENGTH`
- Entity fields: camelCase or matching DB column - `userId`, `username`, `createTime`

**Files:**
- Java files: PascalCase.java matching class name
- MyBatis XML: `{Entity}Mapper.xml` in `src/main/resources/mapper/`
- Extended MyBatis XML: `ExtMapper.xml` in `src/main/resources/mapper/ext/`

---

## Package Organization

**Layered Architecture with CQRS influence:**

1. **controller/** - Thin REST endpoints, delegates to application service
2. **application/** - Use case orchestration, transaction boundaries
3. **service/** - Domain business logic
4. **infra/** - External concerns (DB, cache, security)
5. **model/** - Data structures, split by purpose (entity, dto, request, response)

**Import Organization:**
```java
// 1. JDK packages
import java.util.ArrayList;
import java.util.Date;

// 2. Third-party packages (spring, lombok, etc)
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 3. Internal application packages (grouped by prefix)
import com.ddf.boot.capableadmin.infra.mapper.SysUserMapper;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.capableadmin.model.request.sys.UserProfileUpdateRequest;
import com.ddf.boot.common.api.exception.BusinessException;
```

---

## Annotation Usage

**Controller Layer:**
```java
@RestController                          // REST controller
@RequestMapping("user")                 // URL prefix
@RequiredArgsConstructor                // Lombok - constructor injection
public class UserController {
    @GetMapping("profile")              // GET endpoint
    @PostMapping("profile")             // POST endpoint
    @AdminAuditLog(module = "个人中心", action = "修改个人资料")  // Custom audit annotation
    public ResponseData<Boolean> updateProfile(@RequestBody @Valid UserProfileUpdateRequest request) {
        // ...
    }
}
```

**Application Service Layer:**
```java
@RequiredArgsConstructor(onConstructor_ = {@Autowired})  // Constructor injection with lombok
@Slf4j                                       // Lombok logging
@Service                                     // Spring bean
public class SysUserApplicationService {
    @Transactional(rollbackFor = Exception.class)  // Transaction with rollback
    public void persistUser(SysUserCreateRequest request) {
        // ...
    }
}
```

**Validation Annotations:**
```java
public class SysUserCreateRequest {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotNull(message = "性别不能为空")
    private Integer sex;

    @NotEmpty(message = "角色不能为空")
    private Set<Long> roleIds;
}
```

**Request/Response Objects:**
```java
@Data                          // Lombok - generates getters/setters
public class SysUserCreateRequest implements Serializable {
    // fields with validation annotations
}
```

---

## API Design Patterns

**REST Endpoints:**

| Method | URL Pattern | Purpose |
|--------|-------------|---------|
| GET | `/user/info` | Get current user info |
| GET | `/user/profile` | Get current user profile |
| POST | `/user/profile` | Update profile (with `@AdminAuditLog`) |
| POST | `/user/change-password` | Change password (with `@AdminAuditLog`) |
| POST | `/auth/login` | User login |
| POST | `/auth/logout` | User logout |
| GET | `/auth/codes` | Get current user permission codes |

**Request/Response Envelope:**
```java
// All API responses use ResponseData<T>
public class ResponseData<T> {
    private int code;
    private String message;
    private T data;
    
    public static <T> ResponseData<T> success(T data) { ... }
    public static <T> ResponseData<T> failure(BaseErrorCallbackCode code) { ... }
}
```

**Request Object Pattern:**
```java
@Data
public class SysUserCreateRequest implements Serializable {
    private Long userId;                          // Optional for create
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotNull(message = "性别不能为空")
    private Integer sex;
    @NotNull(message = "状态不能为空")
    private Boolean enabled;
    @NotEmpty(message = "角色不能为空")
    private Set<Long> roleIds;
}
```

**Response Object Pattern:**
```java
@Data
public class SysUserRes {
    private Long userId;
    private String username;
    private String nickname;
    private Integer sex;
    private String mobile;
    private String email;
    private String avatar;
    private Boolean enabled;
    private Set<Long> roleIds;
    private Set<Long> deptIds;
    private Date createTime;
    private Date updateTime;
}
```

---

## Error Handling

**Exception Codes Enum:**
```java
@Getter
public enum PrettyAdminExceptionCode implements BaseCallbackCode {
    SYS_USER_NOT_EXISTS("SYS_USER_NOT_EXISTS", "系统用户不存在"),
    SYS_PASSWORD_NOT_MATCH("SYS_PASSWORD_NOT_MATCH", "系统用户密码不匹配"),
    USER_NAME_EXISTS("USER_NAME_EXISTS", "用户名称已存在"),
    EMAIL_EXISTS("EMAIL_EXISTS", "邮箱已存在"),
    MOBILE_EXISTS("MOBILE_EXISTS", "手机号已存在");

    private final String code;
    private final String description;
    private final String bizMessage;
}
```

**Throwing Business Exceptions:**
```java
// In application service
if (Objects.isNull(sysUser)) {
    throw new BusinessException(PrettyAdminExceptionCode.SYS_USER_NOT_EXISTS);
}
if (!bCryptPasswordEncoder.matches(request.getOldPassword(), sysUser.getPassword())) {
    throw new BusinessException(PrettyAdminExceptionCode.SYS_PASSWORD_NOT_MATCH);
}
```

**Global Exception Handler:**
```java
@RestControllerAdvice
public class PrettyAdminExceptionHandler extends AbstractExceptionHandler {
    @ExceptionHandler(value = {NotLoginException.class})
    public ResponseData<?> handlerNotLoginException(Exception e) {
        return ResponseData.failure(BaseErrorCallbackCode.UNAUTHORIZED);
    }

    @ExceptionHandler(NotPermissionException.class)
    public ResponseData<?> handlerNotPermissionException(NotPermissionException e) {
        return ResponseData.failure(BaseErrorCallbackCode.ACCESS_FORBIDDEN);
    }
}
```

---

## Logging

**Framework:** SLF4J with Lombok `@Slf4j`

**Patterns Used:**
```java
@Slf4j
@Service
public class AdminAuditLogService {
    public void save(SysLog sysLog) {
        try {
            sysLogMapper.insertSelective(sysLog);
        } catch (Exception e) {
            log.error("save audit log failed", e);  // Error with stack trace
        }
    }
}

// In AuthApplicationService
log.info("用户登录成功, userId: {}, username: {}", userId, sysUser.getUsername());
log.info("用户登出,清理缓存, userId: {}", userId);
log.info("用户登出成功, userId: {}", userId);
log.error("用户登出失败", e);
log.debug("保存在线用户信息, userId: {}, token: {}", sysUser.getUserId(), token);
```

**Log Levels:**
- `log.info()` - Business operations (login, logout, profile updates)
- `log.error()` - Failures requiring attention
- `log.debug()` - Detailed debugging information

**Never Log:**
- Passwords or password-related fields (handled by `AdminAuditLogService.sanitizeParams()`)
- Sensitive user data

---

## Custom Annotations

**AdminAuditLog (Method Annotation):**
```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminAuditLog {
    String module();   // Business module name
    String action();   // Operation name
}
```

**Usage:**
```java
@PostMapping("profile")
@AdminAuditLog(module = "个人中心", action = "修改个人资料")
public ResponseData<Boolean> updateProfile(@RequestBody @Valid UserProfileUpdateRequest request) {
    // ...
}
```

---

## Dependency Injection

**Constructor Injection (preferred):**
```java
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class SysUserApplicationService {
    private final SysDeptMapper sysDeptMapper;
    private final SysDeptService sysDeptService;
    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PrettyAdminCacheManager cacheManager;
}
```

**Static Utility with Spring Context:**
```java
public class PrettyAdminSecurityUtils {
    private static final SysUserRepository SYS_USER_REPOSITORY;
    
    static {
        SYS_USER_REPOSITORY = SpringContextHolder.getBean(SysUserRepository.class);
    }
    
    public static Long getCurrentUserId() {
        // ...
    }
}
```

---

## Transaction Management

**Declarative Transactions:**
```java
@Transactional(rollbackFor = Exception.class)
public void persistUser(SysUserCreateRequest request) {
    // All changes in this method are rolled back on any exception
}
```

**Rollback Behavior:**
- `rollbackFor = Exception.class` - Rolls back on any exception (checked or unchecked)

---

*Convention analysis: 2026-04-08*
