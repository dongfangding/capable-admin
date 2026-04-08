# Architecture

**Analysis Date:** 2026-04-08

## Pattern Overview

**Overall:** Layered Architecture with Application Service Pattern

**Key Characteristics:**
- Clear separation of concerns across four main layers: Controller, Application Service, Service, and Data Access (Repository/Mapper)
- Application layer (`application` package) handles use-case orchestration and transaction boundaries
- Service layer (`service` package) contains pure business logic
- Repository layer (`infra/repository`) provides data access abstraction
- MyBatis Mapper layer (`infra/mapper`) handles SQL execution

## Layers

**Controller Layer (`controller/`):**
- Purpose: HTTP request handling and response formatting
- Location: `src/main/java/com/ddf/boot/capableadmin/controller/`
- Contains: REST controllers organized by module (`sys/`, `auth/`)
- Depends on: Application Services
- API style: RESTful with `@RestController`, `@GetMapping`, `@PostMapping`
- Input validation: `@Valid` annotation enables Bean Validation
- Audit logging: `@AdminAuditLog` annotation for operation tracking

**Application Service Layer (`application/`):**
- Purpose: Use-case orchestration, transaction management, and cross-entity operations
- Location: `src/main/java/com/ddf/boot/capableadmin/application/`
- Contains: Application services like `SysUserApplicationService`, `SysDictApplicationService`, `AuthApplicationService`
- Depends on: Mappers, Services, and external components (password encoders, cache managers)
- Transaction boundary: Methods annotated with `@Transactional(rollbackFor = Exception.class)`
- Pattern: Constructor injection via `@RequiredArgsConstructor(onConstructor_ = {@Autowired})`

**Service Layer (`service/`):**
- Purpose: Pure business logic and domain operations
- Location: `src/main/java/com/ddf/boot/capableadmin/service/`
- Contains: Services like `SysMenuService`, `SysDeptService`, `AdminAuditLogService`
- Depends on: Mappers
- Pattern: `@Service` annotated, constructor injection, `@Transactional` for write operations

**Repository Layer (`infra/repository/`):**
- Purpose: Data access abstraction, encapsulates data retrieval logic
- Location: `src/main/java/com/ddf/boot/capableadmin/infra/repository/`
- Contains: Repository classes like `SysMenuRepository`, `SysUserRepository`
- Depends on: MyBatis Mappers
- Pattern: Thin wrapper around Mapper, minimal business logic

**Mapper Layer (`infra/mapper/`):**
- Purpose: SQL execution and database interaction
- Location: `src/main/java/com/ddf/boot/capableadmin/infra/mapper/`
- Contains: MyBatis Mapper interfaces (e.g., `SysMenuMapper`, `SysUserMapper`)
- SQL definitions: Corresponding XML files in `src/main/resources/mapper/`
- Extension pattern: Custom queries in `infra/mapper/ext/` with `*ExtMapper.xml` files
- Mapper scan: `@MapperScan("com.ddf.boot.capableadmin.infra.mapper")` in main application

**Model Layer (`model/`):**
- Purpose: Data representations across the application
- Sub-packages:
  - `entity/`: Database entities (e.g., `SysMenu`, `SysUser`) - annotated with `@Data` (Lombok)
  - `dto/`: Data transfer objects for internal use (e.g., `PrettyAdminUserDetails`)
  - `request/`: API request objects (`*Request`, `*Query`) with validation annotations
  - `response/`: API response objects (`*Response`, `*Res`)
  - `cqrs/`: CQRS result objects (e.g., `SysRoleAdminResult`)

## Data Flow

**Write Operation (e.g., User Creation):**
1. HTTP Request arrives at Controller (`SysUserController`)
2. Controller validates input with `@Valid`, delegates to Application Service
3. Application Service (`SysUserApplicationService`) orchestrates the use case:
   - Performs business validation (duplicate checks)
   - Uses password encoder for security
   - Coordinates multiple mappers for related data
   - Manages transaction boundary
4. Mappers execute SQL via MyBatis
5. Response propagates back through layers

**Read Operation (e.g., User List):**
1. HTTP GET request at Controller
2. Controller delegates to Application Service
3. Application Service uses `PageUtil.startPage()` for pagination
4. Mapper executes query, returns entity list
5. `BeanCopierUtils.copy()` maps entities to response DTOs
6. `PageResult` wraps results for API response

**Authentication Flow:**
1. `AuthController` receives login request
2. `AuthApplicationService` validates credentials
3. SA-Token integration handles session management
4. Returns `PrettyAdminLoginResponse` with user details and token

## Key Abstractions

**Application Service Pattern:**
- Classes: `SysUserApplicationService`, `SysDictApplicationService`, `AuthApplicationService`
- Purpose: Orchestrate complex operations involving multiple entities/repositories
- Pattern: `@Service`, constructor injection, `@Transactional` methods

**Repository Pattern:**
- Classes: `SysMenuRepository`, `SysUserRepository`
- Purpose: Encapsulate data access behind a cleaner interface
- Pattern: Thin wrapper, delegates to Mapper

**Audit Logging:**
- Annotation: `@AdminAuditLog` in `infra/audit/AdminAuditLog.java`
- Aspect: `AdminAuditLogAspect` (from `ddf-common` library) intercepts annotated methods
- Usage: `@AdminAuditLog(module = "用户管理", action = "保存用户")`

**Caching Strategy:**
- Service: `PrettyAdminCacheManager` handles cache operations
- Integration: Redis via `ddf-common-redis` dependency

## Entry Points

**Main Application:**
- Location: `src/main/java/com/ddf/boot/capableadmin/CapableAdminApplication.java`
- Responsibilities: Spring Boot bootstrap, component scan configuration
- Key annotations:
  - `@SpringBootApplication`
  - `@MapperScan("com.ddf.boot.capableadmin.infra.mapper")`
  - `@EnableTransactionManagement`
  - `@EnableAsync`
  - `@EnableScheduling`
  - `@EnableLogAspect` (from ddf-common for access logging)
  - `@EnableRateLimit()` (rate limiting)
  - `@EnableRepeatable(globalValidator = RedisRepeatableValidator.BEAN_NAME)` (duplicate submit prevention)

**Controllers:**
- `controller/sys/*.java` - System management endpoints (user, menu, role, dept, job)
- `controller/auth/*.java` - Authentication endpoints

## Error Handling

**Strategy:** Exception-based with business exception codes

**Business Exceptions:**
- Enum: `PrettyAdminExceptionCode` in `enums/` package
- Thrown via: `throw new BusinessException(PrettyAdminExceptionCode.SYS_USER_NOT_EXISTS)`
- Integration: Handled globally by `ddf-common` framework

**Validation:**
- Bean Validation (`jakarta.validation.constraints.*`)
- Custom validators like `@EnumStringValue` for enum validation

## Cross-Cutting Concerns

**Logging:** SLF4J (`@Slf4j`) with Lombok
**Security:** SA-Token (satoken-spring-boot3-starter) for authentication/authorization
**Transaction Management:** Spring `@Transactional` with `rollbackFor = Exception.class`
**Async Processing:** Spring `@EnableAsync` with custom `ThreadPoolTaskExecutor`
**Rate Limiting:** `ddf-common` `@EnableRateLimit()`
**Duplicate Submit Prevention:** `ddf-common` `@EnableRepeatable`
**Access Logging:** `ddf-common` `@EnableLogAspect`

## Technology Stack

**Framework:** Spring Boot 3.x with Java 17
**Persistence:** MyBatis with XML mapping
**Authentication:** SA-Token (Redisson integration)
**Caching:** Redis via ddf-common-redis
**Base Library:** ddf-common (internal shared library: `ddf-common-starter-default`, `ddf-common-redis`, `ddf-common-s3`)

---

*Architecture analysis: 2026-04-08*
