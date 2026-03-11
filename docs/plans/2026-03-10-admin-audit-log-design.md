# Admin Audit Log Design

**Goal:** Add backend operation audit logging for admin write actions using an annotation plus AOP approach, without changing the existing `sys_log` table schema.

## Scope

- Record only admin operation logs
- Do not record pure query/list endpoints
- Do not include login/logout logs in this phase
- Do not include permission denied or not-logged-in access events in this phase

## Recorded Operations

The first batch should cover write endpoints such as:

- create
- update
- delete
- enable/disable
- reset password
- role-menu binding
- menu sync
- current-user profile update
- current-user password change

## Recorded Fields

Each audit entry should persist into `sys_log` using existing columns:

- `username`: current operator username
- `description`: human-readable action description
- `logType`: success or failure operation type
- `method`: Java method signature or controller method identifier
- `params`: serialized and sanitized request parameters
- `requestIp`: client IP
- `time`: execution duration in milliseconds
- `browser`: `User-Agent` or lightly parsed browser string
- `exceptionDetail`: exception summary for failed operations
- `createTime`: operation timestamp

## Recommended Approach

Use a custom annotation plus AOP:

1. Add `@AdminAuditLog(module, action)` on selected controller methods
2. Use an `@Around` aspect to capture timing, request context, parameters, result, and exception state
3. Delegate persistence to a dedicated service
4. Store into existing `sys_log` through `SysLogMapper.insertSelective`

This is preferred over interceptor-based global logging or manual service-layer logging because:

- operation scope stays explicit
- business meaning is preserved
- logging logic stays centralized
- rollout can be incremental

## Components

### 1. Annotation

Create `@AdminAuditLog` with at least:

- `module`
- `action`

Example:

```java
@AdminAuditLog(module = "用户管理", action = "重置密码")
```

### 2. Aspect

Create `AdminAuditLogAspect`:

- use `@Around`
- capture start time before proceeding
- on success, write a success log
- on exception, write a failure log and rethrow the original exception
- never swallow business exceptions

### 3. Persistence service

Create `AdminAuditLogService`:

- assemble `SysLog`
- persist with `SysLogMapper.insertSelective`
- isolate logging failure from main business flow

### 4. Parameter sanitizer

Create a small sanitizer/helper:

- ignore `HttpServletRequest`, `HttpServletResponse`, uploaded files, streams
- mask values for keys such as:
  - `password`
  - `oldPassword`
  - `newPassword`
  - `token`
- truncate oversized serialized payloads
- fall back to `"<unserializable>"` if JSON conversion fails

## Request Context

The aspect should collect:

- current username from `PrettyAdminSecurityUtils`
- IP from `HttpServletRequest`
- request method and URI from `HttpServletRequest`
- `User-Agent` for browser
- controller method signature from `ProceedingJoinPoint`

If current user resolution fails during logging:

- degrade username to `anonymous`
- do not block the business method

## Error Handling

- Failed business method:
  - still record a failure audit log
  - rethrow the original exception

- Failed log persistence:
  - only print internal error logs
  - never block the business method

- Failed parameter serialization:
  - use fallback text

## Rollout Plan

First batch of annotation targets:

- `UserController`
- `SysUserController`
- `SysRoleController`
- `SysMenuController`
- `SysDeptController`
- `SysJobController`
- `SysDictController`
- `SysDictDetailController`

For `SysLogController`:

- `list` should not be logged
- `delete` may be logged

## Testing Strategy

### Unit tests

- success path writes a success log
- exception path writes a failure log and rethrows
- masked fields are not stored in clear text
- oversized payload is truncated

### Controller-level verification

At least one annotated controller method should be covered end-to-end enough to verify:

- method executes
- audit service is invoked
- `SysLogMapper.insertSelective` is called

### Manual verification

Call these endpoints after implementation:

- `POST /sys-user/reset-password`
- `POST /user/change-password`
- `POST /sys-dict/persist`

Then query:

- `GET /sys-log/list`

Expected:

- successful operations appear as success logs
- forced failures appear as failure logs

## Non-Goals

- login/logout audit logs
- permission-denied audit logs
- database schema changes for `sys_log`
- external log export
- deep browser parsing or geo-IP enrichment
