# Codebase Structure

**Analysis Date:** 2026-04-08

## Directory Layout

```
capable-admin/
├── src/
│   ├── main/
│   │   ├── java/com/ddf/boot/capableadmin/
│   │   │   ├── CapableAdminApplication.java    # Main application entry
│   │   │   ├── application/                    # Application services (use cases)
│   │   │   ├── controller/                    # REST controllers
│   │   │   ├── enums/                         # Enumerations
│   │   │   ├── infra/                         # Infrastructure layer
│   │   │   │   ├── audit/                     # Audit logging
│   │   │   │   ├── config/                    # Configuration classes
│   │   │   │   ├── extra/                     # Extension implementations
│   │   │   │   ├── mapper/                    # MyBatis mappers
│   │   │   │   │   └── ext/                   # Extended mapper queries
│   │   │   │   ├── repository/               # Repository layer
│   │   │   │   └── util/                      # Utility classes
│   │   │   ├── model/                         # Domain models
│   │   │   │   ├── cqrs/                      # CQRS result objects
│   │   │   │   ├── dto/                       # Data transfer objects
│   │   │   │   ├── entity/                   # Database entities
│   │   │   │   ├── request/                   # API request objects
│   │   │   │   └── response/                  # API response objects
│   │   │   └── service/                      # Business services
│   │   └── resources/
│   │       ├── mapper/                        # MyBatis XML mappers
│   │       │   └── ext/                       # Extended mapper XMLs
│   │       ├── static/                        # Static resources
│   │       └── doc/                           # Documentation
│   └── test/
│       └── java/com/ddf/boot/capableadmin/    # Test sources (mirrors main)
├── pom.xml                                    # Maven configuration
└── target/                                   # Build output
```

## Directory Purposes

**`application/` (Application Layer):**
- Purpose: Use-case orchestration and transaction management
- Contains: Application service classes
- Key files:
  - `SysUserApplicationService.java` - User management use cases
  - `SysDictApplicationService.java` - Dictionary management use cases
  - `AuthApplicationService.java` - Authentication use cases
  - `SysLogApplicationService.java` - Operation log use cases
- Pattern: `@Service` annotated, constructor injection via `@RequiredArgsConstructor`

**`controller/` (Controller Layer):**
- Purpose: HTTP request handling
- Contains: REST controllers organized by module
- Sub-packages:
  - `sys/` - System management controllers (user, menu, role, dept, job, dict, log)
  - `auth/` - Authentication controllers

**`service/` (Service Layer):**
- Purpose: Pure business logic
- Contains: Domain services
- Key files:
  - `SysMenuService.java` - Menu tree building, CRUD operations
  - `SysDeptService.java` - Department management
  - `SysRoleService.java` - Role management
  - `AdminAuditLogService.java` - Audit log service
  - `PrettyAdminUserDetailsService.java` - User details for authentication
  - `PrettyAdminCacheManager.java` - Cache management

**`infra/mapper/` (Data Access Layer):**
- Purpose: MyBatis mapper interfaces
- Contains: Mapper interfaces for each entity
- Key files:
  - `SysMenuMapper.java`
  - `SysUserMapper.java`
  - `SysDeptMapper.java`
  - `SysRoleMapper.java`
  - `SysLogMapper.java`
  - `SysDictMapper.java`
  - `SysDictDetailMapper.java`
- Extension mappers in `infra/mapper/ext/`:
  - `SysMenuExtMapper.java`, `SysMenuExtMapper.xml`
  - `SysUserExtMapper.java`, `SysUserExtMapper.xml`
  - `SysDictExtMapper.java`, `SysDictExtMapper.xml`

**`infra/repository/` (Repository Layer):**
- Purpose: Data access abstraction
- Contains: Repository implementations wrapping mappers
- Key files:
  - `SysMenuRepository.java`
  - `SysUserRepository.java`

**`model/entity/` (Database Entities):**
- Purpose: Database table representations
- Contains: JPA-lite entities (Lombok `@Data`, no JPA annotations)
- Key files:
  - `SysMenu.java`
  - `SysUser.java`
  - `SysDept.java`
  - `SysRole.java`
  - `SysLog.java`
  - `SysDict.java`
  - `SysDictDetail.java`

**`model/request/` (Request DTOs):**
- Purpose: API input validation
- Naming: `*Request` (create/update), `*Query` (search), `*ListRequest` (paginated list)
- Sub-packages mirror controller modules (`sys/`, `auth/`)
- Key files:
  - `SysUserCreateRequest.java`
  - `SysUserListRequest.java`
  - `SysMenuCreateRequest.java`
  - `SysMenuListQuery.java`

**`model/response/` (Response DTOs):**
- Purpose: API output formatting
- Naming: `*Response`, `*Res`, `*Node`, `*Result`
- Sub-packages mirror controller modules (`sys/`, `auth/`)
- Key files:
  - `SysUserRes.java`
  - `SysMenuRes.java`
  - `SysMenuNode.java` (tree nodes)
  - `MenuRouteNode.java` (frontend route config)
  - `PageResult.java` (pagination wrapper from ddf-common)

**`infra/config/` (Configuration):**
- Purpose: Spring configuration classes
- Key files:
  - `WebConfig.java` - Static resource handling, cache control
  - `PrettyAdminThreadConfig.java` - Custom thread pool (`userLoginHistoryExecutor`)
  - `PrettyAdminWebConfig.java` - Web-specific configuration
  - `CapableAdminThreadConfig.java` - Thread pool configuration

**`infra/audit/` (Audit Logging):**
- Purpose: Operation audit infrastructure
- Key files:
  - `AdminAuditLog.java` - Annotation for marking auditable operations

**`infra/util/` (Utilities):**
- Purpose: Helper classes and security utilities
- Key files:
  - `PrettyAdminSecurityUtils.java` - Security context access
  - `CapableAdminUtils.java` - General utilities

**`enums/` (Enumerations):**
- Purpose: Type-safe constants
- Key files:
  - `PrettyAdminExceptionCode.java` - Business exception codes
  - `MenuTypeEnum.java` - Menu type constants (CATALOG, MENU, BUTTON, etc.)

## Key File Locations

**Entry Points:**
- `src/main/java/com/ddf/boot/capableadmin/CapableAdminApplication.java` - Spring Boot main class

**Configuration:**
- `pom.xml` - Maven dependencies and build configuration
- `src/main/java/com/ddf/boot/capableadmin/infra/config/*.java` - Spring Java config

**MyBatis Mappers:**
- Interface: `src/main/java/com/ddf/boot/capableadmin/infra/mapper/*.java`
- XML: `src/main/resources/mapper/*.xml`
- Extended: `src/main/resources/mapper/ext/*.xml`

**Core Logic:**
- Controllers: `src/main/java/com/ddf/boot/capableadmin/controller/**/*.java`
- Application Services: `src/main/java/com/ddf/boot/capableadmin/application/*.java`
- Services: `src/main/java/com/ddf/boot/capableadmin/service/*.java`

**Testing:**
- `src/test/java/com/ddf/boot/capableadmin/application/*.java` - Application service tests
- `src/test/java/com/ddf/boot/capableadmin/controller/**/*.java` - Controller tests
- `src/test/java/com/ddf/boot/capableadmin/service/*.java` - Service tests

## Naming Conventions

**Java Files:**
- Classes: PascalCase (e.g., `SysUserController`, `SysMenuService`)
- Interfaces: PascalCase with `*Mapper`, `*Repository`, `*Service` suffixes
- Enums: PascalCase (e.g., `MenuTypeEnum`, `PrettyAdminExceptionCode`)

**Package Names:**
- All lowercase, dot-separated (e.g., `com.ddf.boot.capableadmin.controller.sys`)
- One package per layer concern

**MyBatis:**
- Mapper XML: Entity name + `Mapper.xml` (e.g., `SysMenuMapper.xml`)
- Extended queries: `*ExtMapper.xml` in `mapper/ext/` directory
- ResultMap: `BaseResultMap` convention

**Test Files:**
- Class name: `*Test` suffix (e.g., `SysUserApplicationServiceTest`)
- Mirrors main source structure

## Where to Add New Code

**New Entity/Table:**
1. Create entity: `model/entity/NewEntity.java` (Lombok `@Data`)
2. Create mapper interface: `infra/mapper/NewEntityMapper.java`
3. Create mapper XML: `resources/mapper/NewEntityMapper.xml`
4. Create repository (optional): `infra/repository/NewEntityRepository.java`
5. Create service: `service/NewEntityService.java`
6. Create application service: `application/NewEntityApplicationService.java`
7. Create controller: `controller/module/NewEntityController.java`
8. Create request DTOs: `model/request/module/NewEntity*Request.java`
9. Create response DTOs: `model/response/module/NewEntity*Response.java`
10. Add tests: `src/test/java/com/ddf/boot/capableadmin/...`

**Custom SQL Queries (not auto-generated):**
- Create in `resources/mapper/ext/` directory with `ExtMapper.xml` naming
- Create corresponding interface in `infra/mapper/ext/` with `ExtMapper` suffix
- Example: `SysMenuExtMapper.java` + `SysMenuExtMapper.xml`

**New Module/Subsystem:**
- Follow existing package structure under `controller/`, `service/`, `application/`
- Add sub-package under `model/request/` and `model/response/`

**Configuration Classes:**
- Location: `infra/config/` with `*Config.java` naming
- Annotate with `@Configuration`

## Special Directories

**`resources/mapper/`:**
- Purpose: MyBatis XML mapper files
- Contains: Auto-generated base CRUD + custom queries
- NOT modified directly for custom SQL - use `resources/mapper/ext/` instead

**`resources/mapper/ext/`:**
- Purpose: Extended/custom SQL queries
- Pattern: `ExtMapper.xml` files for complex queries
- This is where custom SQL should be added per project conventions

**`resources/static/`:**
- Purpose: Static web resources (JS, CSS, images)
- Cache configured via `WebConfig.java` (365 days for hashed assets)

---

*Structure analysis: 2026-04-08*
