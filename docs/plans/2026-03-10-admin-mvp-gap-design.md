# Admin MVP Gap Design

**Goal:** Complete the missing MVP backend capabilities for the admin server so the frontend can rely on real permissions, self-service profile management, system logs, and dictionary management.

**Context**

The current project already provides:
- Authentication endpoints: `auth/login`, `auth/logout`
- User-facing endpoint: `user/info`
- Admin CRUD around users, roles, menus, departments, and jobs
- Partial cache invalidation infrastructure in `PrettyAdminCacheManager`
- User permission aggregation in `PrettyAdminUserDetailsService`

The current gaps are:
- `GET /auth/codes` returns hard-coded placeholder values
- `StpInterfaceImpl` returns mock permission and role data
- No personal profile update or password change endpoint for the current user
- `sys_log`, `sys_dict`, and `sys_dict_detail` have tables and generated mappers but no usable admin APIs
- `PrettyAdminSecurityUtils.getCurrentUser()` assumes session data always exists and can throw null pointer errors

## Scope

### 1. Permission closure

- Keep `GET /auth/codes`
- Return real permission codes for the current logged-in user
- Make Sa-Token permission and role loading consistent with the same business source

### 2. Personal center

- Add `GET /user/profile` for editable current-user profile data
- Add `POST /user/profile` to update nickname, email, mobile, avatar, and sex in one request
- Add `POST /user/change-password` for current-user password changes
- Do not allow personal-center APIs to modify role, department, job, or enabled status
- Avatar handling only stores the avatar URL; no upload capability is included in this scope

### 3. System log management

- Add `GET /sys-log/list`
- Support filtering by username, log type, method, and time range
- Add `POST /sys-log/delete`
- Delete can be idempotent to simplify repeated frontend operations

### 4. Dictionary management

- Add `GET /sys-dict/list`, `POST /sys-dict/persist`, `POST /sys-dict/delete`
- Add `GET /sys-dict-detail/list`, `POST /sys-dict-detail/persist`, `POST /sys-dict-detail/delete`
- New query SQL must be implemented only in `src/main/resources/mapper/ext/*ExtMapper.xml`
- New custom mapper interfaces must be implemented only in `.../infra/mapper/ext/*ExtMapper.java`

## API Design

### Permissions

#### `GET /auth/codes`

Response:
- Current user permission code list

Rules:
- If the current user is admin, reuse the full permission set already assembled by `PrettyAdminUserDetailsService`
- If the user is not logged in, the existing auth mechanism should reject the request

Implementation note:
- Prefer reading from `PrettyAdminSecurityUtils.getCurrentUser()` so `auth/codes` and `StpInterfaceImpl` stay consistent

### Personal center

#### `GET /user/profile`

Response fields:
- `userId`
- `username`
- `nickname`
- `email`
- `mobile`
- `avatar`
- `sex`

#### `POST /user/profile`

Request fields:
- `nickname`
- `email`
- `mobile`
- `avatar`
- `sex`

Rules:
- Operates on current login user only
- `email` and `mobile` must remain unique across users, excluding the current user
- Successful update must invalidate current-user cache entries

#### `POST /user/change-password`

Request fields:
- `oldPassword`
- `newPassword`

Rules:
- Validate old password against current user password hash
- Reject when old and new passwords are the same
- Update password and password reset time
- Invalidate current-user cache after success
- Optional logout-after-change is not required in this phase

### System logs

#### `GET /sys-log/list`

Query fields:
- `username`
- `logType`
- `method`
- `startTime`
- `endTime`
- pagination fields based on existing page request conventions

Response:
- Paginated `sys_log` records

#### `POST /sys-log/delete`

Request:
- Single id or id collection, depending on the shared request model chosen during implementation

Rules:
- Missing ids do not need to raise business errors

### Dictionaries

#### `GET /sys-dict/list`

Query fields:
- `name`
- pagination fields

#### `POST /sys-dict/persist`

Request fields:
- `dictId` for update
- `name`
- `description`

#### `POST /sys-dict/delete`

Request:
- `dictId`

Rule:
- Deleting a dictionary should also delete its details, or explicitly reject if details exist. Default recommendation: cascade delete in application logic.

#### `GET /sys-dict-detail/list`

Query fields:
- `dictId`
- `label`
- `value`
- pagination fields

#### `POST /sys-dict-detail/persist`

Request fields:
- `detailId` for update
- `dictId`
- `label`
- `value`
- `dictSort`

#### `POST /sys-dict-detail/delete`

Request:
- `detailId`

## Architecture

### Permission data flow

1. Login creates session and stores `USER_DETAILS`
2. `PrettyAdminUserDetailsService` remains the single place to assemble roles and permissions
3. `PrettyAdminSecurityUtils` reads current user details from session safely
4. `AuthController.codes()` returns current-user permissions from session data
5. `StpInterfaceImpl` returns permissions and roles from the same current-user detail source when available

### Personal center data flow

1. Controller receives current-user request
2. Application service resolves current user id through `PrettyAdminSecurityUtils`
3. Service validates uniqueness and password rules
4. Mapper updates current user record
5. `PrettyAdminCacheManager.cleanUserAllCache(userId)` clears stale cache

### Log and dictionary architecture

- Keep generated mapper XML files untouched
- Add ext mappers for list and conditional query requirements
- Keep create/update/delete in application or service layer and use generated mappers where possible
- Use ext mapper only for custom list and association SQL

## Error Handling

- `PrettyAdminSecurityUtils.getCurrentUser()` must fail with an auth exception when session user details are missing instead of throwing null pointer exceptions
- `POST /user/profile` must reject duplicated email or mobile values owned by another user
- `POST /user/change-password` must reject:
  - non-existent current user
  - wrong old password
  - same old and new password
- `GET /auth/codes` should rely on existing login checks rather than returning placeholder data
- Delete operations for logs can be idempotent

## Testing Strategy

### Priority 1

- Verify real permission codes are returned for the current login user
- Verify `StpInterfaceImpl` and `auth/codes` stay consistent for the same user
- Verify personal profile update clears current-user cache
- Verify password change fails when old password is wrong

### Priority 2

- Verify system log paginated query works with filters
- Verify dictionary and dictionary-detail CRUD basic flows work

### Minimum verification if test infrastructure is weak

- Run project compilation
- Run targeted controller/application tests if available
- Perform manual API verification for:
  - `GET /auth/codes`
  - `GET/POST /user/profile`
  - `POST /user/change-password`
  - `GET /sys-log/list`
  - dictionary CRUD endpoints

## Delivery Order

### Phase 1

- Real permission code endpoint
- Sa-Token permission and role integration
- Personal profile update
- Password change
- Safer current-user resolution

### Phase 2

- System log query and delete APIs

### Phase 3

- Dictionary and dictionary-detail management APIs

## Explicit Non-Goals

- Avatar file upload
- Log export
- Dictionary cache optimization
- Broader auth model redesign
