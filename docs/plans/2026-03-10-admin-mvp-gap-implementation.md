# Admin MVP Gap Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Build the missing MVP backend capabilities for real permission codes, current-user profile management, password change, system log management, and dictionary management.

**Architecture:** Keep permission assembly centered on `PrettyAdminUserDetailsService`, expose current-user APIs through `UserController`, and add list-query SQL only through `infra/mapper/ext` plus `resources/mapper/ext`. Reuse generated CRUD mappers where possible, and invalidate user cache through `PrettyAdminCacheManager` after profile or password changes. Implement in thin vertical slices so each slice can be compiled and verified independently.

**Tech Stack:** Spring Boot 3, Sa-Token, MyBatis, Lombok, Maven, JUnit 5 / Spring Boot Test

---

### Task 1: Add test scaffolding for current-user permission and profile flows

**Files:**
- Create: `src/test/java/com/ddf/boot/capableadmin/application/AuthApplicationServiceTest.java`
- Create: `src/test/java/com/ddf/boot/capableadmin/application/SysUserApplicationServiceTest.java`
- Create: `src/test/java/com/ddf/boot/capableadmin/infra/config/StpInterfaceImplTest.java`
- Modify: `pom.xml` only if current test runtime is missing a dependency discovered during execution

**Step 1: Write the failing permission test**

```java
@Test
void shouldReturnCurrentUserPermissionsFromUserDetails() {
    PrettyAdminUserDetails details = new PrettyAdminUserDetails();
    details.setPermissions(Set.of("System:Menu:List", "System:User:Edit"));
    // mock current-user resolution and assert returned permissions
}
```

**Step 2: Run test to verify it fails**

Run: `mvn -q -Dtest=AuthApplicationServiceTest test`
Expected: FAIL because permission endpoint/service path is not wired for current-user permission output yet

**Step 3: Write the failing profile update test**

```java
@Test
void shouldClearUserCacheAfterProfileUpdate() {
    Long userId = 1L;
    // mock current user, mapper update success, and verify cacheManager.cleanUserAllCache(userId)
}
```

**Step 4: Run test to verify it fails**

Run: `mvn -q -Dtest=SysUserApplicationServiceTest test`
Expected: FAIL because profile update API/application method does not exist yet

**Step 5: Write the failing Sa-Token permission integration test**

```java
@Test
void shouldReadPermissionsAndRolesFromCurrentUserDetails() {
    // mock current user details and assert getPermissionList/getRoleList reflect the same source
}
```

**Step 6: Run test to verify it fails**

Run: `mvn -q -Dtest=StpInterfaceImplTest test`
Expected: FAIL because `StpInterfaceImpl` still returns mock data

**Step 7: Commit**

```bash
git add src/test/java/com/ddf/boot/capableadmin/application/AuthApplicationServiceTest.java src/test/java/com/ddf/boot/capableadmin/application/SysUserApplicationServiceTest.java src/test/java/com/ddf/boot/capableadmin/infra/config/StpInterfaceImplTest.java pom.xml
git commit -m "test: add admin mvp permission and profile scaffolding"
```

### Task 2: Implement real permission code endpoint and safe current-user resolution

**Files:**
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/auth/AuthController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/infra/config/StpInterfaceImpl.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/infra/util/PrettyAdminSecurityUtils.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/model/dto/PrettyAdminUserDetails.java` only if it lacks getters needed by tests
- Test: `src/test/java/com/ddf/boot/capableadmin/application/AuthApplicationServiceTest.java`
- Test: `src/test/java/com/ddf/boot/capableadmin/infra/config/StpInterfaceImplTest.java`

**Step 1: Write minimal implementation for safe current-user access**

```java
public static PrettyAdminUserDetails getCurrentUser() {
    StpUtil.checkLogin();
    Object sessionValue = StpUtil.getSession(true)
        .get(PrettyAdminRedisKeyEnum.USER_DETAILS.getKey(Long.toString(StpUtil.getLoginIdAsLong())));
    if (sessionValue == null) {
        throw new UnauthorizedException("USER_DETAILS missing");
    }
    return JsonUtil.toBean(sessionValue.toString(), PrettyAdminUserDetails.class);
}
```

**Step 2: Make `auth/codes` return real permissions**

```java
@GetMapping("codes")
public ResponseData<List<String>> codes() {
    return ResponseData.success(new ArrayList<>(PrettyAdminSecurityUtils.getCurrentUser().getPermissions()));
}
```

**Step 3: Replace mock Sa-Token permission and role values**

```java
@Override
public List<String> getPermissionList(Object loginId, String loginType) {
    return new ArrayList<>(PrettyAdminSecurityUtils.getCurrentUser().getPermissions());
}
```

**Step 4: Run targeted tests**

Run: `mvn -q -Dtest=AuthApplicationServiceTest,StpInterfaceImplTest test`
Expected: PASS

**Step 5: Run compile verification**

Run: `mvn -q -DskipTests compile`
Expected: BUILD SUCCESS

**Step 6: Commit**

```bash
git add src/main/java/com/ddf/boot/capableadmin/controller/auth/AuthController.java src/main/java/com/ddf/boot/capableadmin/infra/config/StpInterfaceImpl.java src/main/java/com/ddf/boot/capableadmin/infra/util/PrettyAdminSecurityUtils.java src/main/java/com/ddf/boot/capableadmin/model/dto/PrettyAdminUserDetails.java src/test/java/com/ddf/boot/capableadmin/application/AuthApplicationServiceTest.java src/test/java/com/ddf/boot/capableadmin/infra/config/StpInterfaceImplTest.java
git commit -m "feat: return real permission codes for current user"
```

### Task 3: Implement current-user profile query, profile update, and password change

**Files:**
- Create: `src/main/java/com/ddf/boot/capableadmin/model/request/sys/UserProfileUpdateRequest.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/model/request/sys/UserChangePasswordRequest.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/model/response/sys/UserProfileResponse.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/controller/sys/UserController.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/application/SysUserApplicationService.java`
- Modify: `src/main/java/com/ddf/boot/capableadmin/infra/mapper/SysUserMapper.java`
- Modify: `src/main/resources/mapper/SysUserMapper.xml` only if a generated statement already exists and only for non-custom SQL already represented there; otherwise prefer generated methods already available in mapper
- Test: `src/test/java/com/ddf/boot/capableadmin/application/SysUserApplicationServiceTest.java`

**Step 1: Write failing tests for profile read/update and password change**

```java
@Test
void shouldReturnCurrentUserProfile() {}

@Test
void shouldRejectPasswordChangeWhenOldPasswordDoesNotMatch() {}
```

**Step 2: Run tests to confirm failure**

Run: `mvn -q -Dtest=SysUserApplicationServiceTest test`
Expected: FAIL because request/response models and controller methods do not exist yet

**Step 3: Add request and response models**

```java
public class UserProfileUpdateRequest {
    private String nickname;
    private String email;
    private String mobile;
    private String avatar;
    private Integer sex;
}
```

**Step 4: Add application methods**

```java
public UserProfileResponse getCurrentUserProfile() { ... }
public void updateCurrentUserProfile(UserProfileUpdateRequest request) { ... }
public void changeCurrentUserPassword(UserChangePasswordRequest request) { ... }
```

**Step 5: Add controller endpoints**

```java
@GetMapping("profile")
public ResponseData<UserProfileResponse> profile() { ... }

@PostMapping("profile")
public ResponseData<Boolean> updateProfile(@RequestBody @Valid UserProfileUpdateRequest request) { ... }

@PostMapping("change-password")
public ResponseData<Boolean> changePassword(@RequestBody @Valid UserChangePasswordRequest request) { ... }
```

**Step 6: Invalidate cache after successful update**

```java
cacheManager.cleanUserAllCache(userId);
```

**Step 7: Run targeted tests**

Run: `mvn -q -Dtest=SysUserApplicationServiceTest test`
Expected: PASS

**Step 8: Run compile verification**

Run: `mvn -q -DskipTests compile`
Expected: BUILD SUCCESS

**Step 9: Commit**

```bash
git add src/main/java/com/ddf/boot/capableadmin/model/request/sys/UserProfileUpdateRequest.java src/main/java/com/ddf/boot/capableadmin/model/request/sys/UserChangePasswordRequest.java src/main/java/com/ddf/boot/capableadmin/model/response/sys/UserProfileResponse.java src/main/java/com/ddf/boot/capableadmin/controller/sys/UserController.java src/main/java/com/ddf/boot/capableadmin/application/SysUserApplicationService.java src/main/java/com/ddf/boot/capableadmin/infra/mapper/SysUserMapper.java src/test/java/com/ddf/boot/capableadmin/application/SysUserApplicationServiceTest.java
git commit -m "feat: add current user profile and password APIs"
```

### Task 4: Add ext mapper and API slice for system log management

**Files:**
- Create: `src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysLogListRequest.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/model/response/sys/SysLogResponse.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/application/SysLogApplicationService.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysLogController.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/infra/mapper/ext/SysLogExtMapper.java`
- Create: `src/main/resources/mapper/ext/SysLogExtMapper.xml`
- Create: `src/test/java/com/ddf/boot/capableadmin/application/SysLogApplicationServiceTest.java`

**Step 1: Write the failing log list test**

```java
@Test
void shouldReturnPagedLogsByFilter() {}
```

**Step 2: Run test to verify it fails**

Run: `mvn -q -Dtest=SysLogApplicationServiceTest test`
Expected: FAIL because service and ext mapper do not exist yet

**Step 3: Add ext mapper interface and XML query**

```java
List<SysLogResponse> listByCondition(SysLogListRequest request);
```

```xml
<select id="listByCondition" resultType="com.ddf.boot.capableadmin.model.response.sys.SysLogResponse">
  SELECT ...
  FROM sys_log
  <where>...</where>
  ORDER BY create_time DESC
</select>
```

**Step 4: Add application service and controller**

```java
public PageResult<SysLogResponse> list(SysLogListRequest request) { ... }
public void deleteByIds(Set<Long> ids) { ... }
```

**Step 5: Run targeted tests**

Run: `mvn -q -Dtest=SysLogApplicationServiceTest test`
Expected: PASS

**Step 6: Run compile verification**

Run: `mvn -q -DskipTests compile`
Expected: BUILD SUCCESS

**Step 7: Commit**

```bash
git add src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysLogListRequest.java src/main/java/com/ddf/boot/capableadmin/model/response/sys/SysLogResponse.java src/main/java/com/ddf/boot/capableadmin/application/SysLogApplicationService.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysLogController.java src/main/java/com/ddf/boot/capableadmin/infra/mapper/ext/SysLogExtMapper.java src/main/resources/mapper/ext/SysLogExtMapper.xml src/test/java/com/ddf/boot/capableadmin/application/SysLogApplicationServiceTest.java
git commit -m "feat: add system log management APIs"
```

### Task 5: Add ext mapper and API slice for dictionary and dictionary detail management

**Files:**
- Create: `src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysDictListRequest.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysDictPersistRequest.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysDictDetailListRequest.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysDictDetailPersistRequest.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/model/response/sys/SysDictResponse.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/model/response/sys/SysDictDetailResponse.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/application/SysDictApplicationService.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDictController.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDictDetailController.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/infra/mapper/ext/SysDictExtMapper.java`
- Create: `src/main/java/com/ddf/boot/capableadmin/infra/mapper/ext/SysDictDetailExtMapper.java`
- Create: `src/main/resources/mapper/ext/SysDictExtMapper.xml`
- Create: `src/main/resources/mapper/ext/SysDictDetailExtMapper.xml`
- Create: `src/test/java/com/ddf/boot/capableadmin/application/SysDictApplicationServiceTest.java`

**Step 1: Write the failing dictionary tests**

```java
@Test
void shouldListDictionariesByName() {}

@Test
void shouldCascadeDeleteDetailsWhenDeletingDict() {}
```

**Step 2: Run test to verify it fails**

Run: `mvn -q -Dtest=SysDictApplicationServiceTest test`
Expected: FAIL because dictionary services/controllers/ext mappers do not exist yet

**Step 3: Add ext mapper queries**

```xml
<select id="listDicts" resultType="com.ddf.boot.capableadmin.model.response.sys.SysDictResponse">
  SELECT ...
</select>
```

```xml
<select id="listDetails" resultType="com.ddf.boot.capableadmin.model.response.sys.SysDictDetailResponse">
  SELECT ...
</select>
```

**Step 4: Add application services for CRUD and cascade delete**

```java
public void deleteDict(Long dictId) {
    sysDictDetailExtMapper.deleteByDictId(dictId);
    sysDictMapper.deleteByPrimaryKey(dictId);
}
```

**Step 5: Add controllers**

```java
@GetMapping("list")
@PostMapping("persist")
@PostMapping("delete")
```

**Step 6: Run targeted tests**

Run: `mvn -q -Dtest=SysDictApplicationServiceTest test`
Expected: PASS

**Step 7: Run compile verification**

Run: `mvn -q -DskipTests compile`
Expected: BUILD SUCCESS

**Step 8: Commit**

```bash
git add src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysDictListRequest.java src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysDictPersistRequest.java src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysDictDetailListRequest.java src/main/java/com/ddf/boot/capableadmin/model/request/sys/SysDictDetailPersistRequest.java src/main/java/com/ddf/boot/capableadmin/model/response/sys/SysDictResponse.java src/main/java/com/ddf/boot/capableadmin/model/response/sys/SysDictDetailResponse.java src/main/java/com/ddf/boot/capableadmin/application/SysDictApplicationService.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDictController.java src/main/java/com/ddf/boot/capableadmin/controller/sys/SysDictDetailController.java src/main/java/com/ddf/boot/capableadmin/infra/mapper/ext/SysDictExtMapper.java src/main/java/com/ddf/boot/capableadmin/infra/mapper/ext/SysDictDetailExtMapper.java src/main/resources/mapper/ext/SysDictExtMapper.xml src/main/resources/mapper/ext/SysDictDetailExtMapper.xml src/test/java/com/ddf/boot/capableadmin/application/SysDictApplicationServiceTest.java
git commit -m "feat: add dictionary management APIs"
```

### Task 6: Run final verification and prepare review handoff

**Files:**
- Modify: `README.md` only if API usage documentation is required after implementation
- Modify: `docs/plans/2026-03-10-admin-mvp-gap-design.md` only if implementation reveals a necessary design delta
- Test: existing and newly created test files under `src/test/java/com/ddf/boot/capableadmin/`

**Step 1: Run the focused test suite**

Run: `mvn -q -Dtest=AuthApplicationServiceTest,SysUserApplicationServiceTest,StpInterfaceImplTest,SysLogApplicationServiceTest,SysDictApplicationServiceTest test`
Expected: PASS

**Step 2: Run full compile verification**

Run: `mvn -q -DskipTests compile`
Expected: BUILD SUCCESS

**Step 3: Inspect git diff**

Run: `git status --short`
Expected: only intended implementation files are modified or added

**Step 4: Commit**

```bash
git add .
git commit -m "feat: complete admin mvp backend gaps"
```
