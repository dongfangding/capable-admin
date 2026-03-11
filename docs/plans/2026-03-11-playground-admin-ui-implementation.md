# Playground Admin UI Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Complete the `playground` admin frontend so it supports the newly available backend profile, job, dictionary, and system log capabilities.

**Architecture:** Extend the existing `playground` page pattern instead of introducing a new frontend architecture. Add focused API modules, keep route declarations under `playground/src/router/routes/modules/system.ts`, and reuse the current list/data/form composition style for new system pages.

**Tech Stack:** Vue 3, TypeScript, Vben Admin playground, Ant Design Vue, existing VXE table adapter, existing API request client

---

### Task 1: Add failing profile integration tests or type-level checks

**Files:**
- Modify: `playground/src/views/_core/profile/base-setting.vue`
- Modify: `playground/src/api/core/user.ts`
- Test: `playground/src/__tests__/` only if a suitable frontend test pattern already exists

**Step 1: Identify the current profile component contract**

Read:
- `playground/src/views/_core/profile/base-setting.vue`
- any related password/profile components under `packages/effects/common-ui/src/ui/profile/`

**Step 2: Write the failing profile API wiring change**

Replace the mock-oriented field assumptions in the page design with the intended real backend fields:

```ts
await getUserProfileApi();
await updateUserProfileApi(formValues);
await changePasswordApi(passwordValues);
```

**Step 3: Run type check or build to verify it fails**

Run: `pnpm --dir playground build`
Expected: FAIL because new API functions and/or page fields do not exist yet

**Step 4: Commit**

```bash
git add playground/src/views/_core/profile/base-setting.vue playground/src/api/core/user.ts
git commit -m "test: scaffold playground profile integration changes"
```

### Task 2: Implement profile API integration

**Files:**
- Modify: `playground/src/api/core/user.ts`
- Modify: `playground/src/api/index.ts`
- Modify: `playground/src/views/_core/profile/base-setting.vue`
- Create or modify: profile password-related component under `playground/src/views/_core/profile/` if needed

**Step 1: Add profile API methods**

```ts
export async function getUserProfileApi() {
  return requestClient.get('/user/profile');
}

export async function updateUserProfileApi(data) {
  return requestClient.post('/user/profile', data);
}

export async function changePasswordApi(data) {
  return requestClient.post('/user/change-password', data);
}
```

**Step 2: Refactor the base profile page**

Use real fields:

- nickname
- username
- email
- mobile
- avatar
- sex

**Step 3: Add password form integration**

Use a separate submit path for password changes.

**Step 4: Run verification**

Run: `pnpm --dir playground build`
Expected: PASS

**Step 5: Commit**

```bash
git add playground/src/api/core/user.ts playground/src/api/index.ts playground/src/views/_core/profile/base-setting.vue
git commit -m "feat: connect playground profile to backend APIs"
```

### Task 3: Add job API layer and page

**Files:**
- Create: `playground/src/api/system/job.ts`
- Modify: `playground/src/api/system/index.ts`
- Modify: `playground/src/api/index.ts`
- Create: `playground/src/views/system/job/list.vue`
- Create: `playground/src/views/system/job/data.ts`
- Create: `playground/src/views/system/job/modules/form.vue`
- Modify: `playground/src/router/routes/modules/system.ts`

**Step 1: Write the failing page scaffold**

Mirror existing system pages and reference the intended APIs:

```ts
getJobList(...)
createJob(...)
updateJob(...)
deleteJob(...)
```

**Step 2: Run build to verify failure**

Run: `pnpm --dir playground build`
Expected: FAIL until job API functions and view files exist

**Step 3: Implement the API layer**

Add backend calls for:

- list/query
- persist
- delete

**Step 4: Implement the page**

Reuse the list/data/form pattern already used by:

- `playground/src/views/system/user/`
- `playground/src/views/system/role/`

**Step 5: Add route**

Register `/system/job`.

**Step 6: Run verification**

Run: `pnpm --dir playground build`
Expected: PASS

**Step 7: Commit**

```bash
git add playground/src/api/system/job.ts playground/src/api/system/index.ts playground/src/api/index.ts playground/src/views/system/job playground/src/router/routes/modules/system.ts
git commit -m "feat: add playground job management page"
```

### Task 4: Add dictionary API layer and page

**Files:**
- Create: `playground/src/api/system/dict.ts`
- Modify: `playground/src/api/system/index.ts`
- Modify: `playground/src/api/index.ts`
- Create: `playground/src/views/system/dict/list.vue`
- Create: `playground/src/views/system/dict/data.ts`
- Create: `playground/src/views/system/dict/modules/form.vue`
- Create: `playground/src/views/system/dict/modules/detail-modal.vue`
- Modify: `playground/src/router/routes/modules/system.ts`

**Step 1: Write the failing page structure**

Reference intended functions:

```ts
getDictList(...)
createDict(...)
updateDict(...)
deleteDict(...)
getDictDetailList(...)
createDictDetail(...)
updateDictDetail(...)
deleteDictDetail(...)
```

**Step 2: Run build to verify failure**

Run: `pnpm --dir playground build`
Expected: FAIL until all APIs and components exist

**Step 3: Implement dictionary APIs**

Support:

- dict list/persist/delete
- detail list/persist/delete

**Step 4: Implement dictionary page**

Include:

- table
- form drawer/modal
- row action to open detail modal

**Step 5: Implement detail modal**

Include:

- detail list
- inline actions or nested form modal

**Step 6: Add route**

Register `/system/dict`

**Step 7: Run verification**

Run: `pnpm --dir playground build`
Expected: PASS

**Step 8: Commit**

```bash
git add playground/src/api/system/dict.ts playground/src/api/system/index.ts playground/src/api/index.ts playground/src/views/system/dict playground/src/router/routes/modules/system.ts
git commit -m "feat: add playground dictionary management page"
```

### Task 5: Add system log API layer and page

**Files:**
- Create: `playground/src/api/system/log.ts`
- Modify: `playground/src/api/system/index.ts`
- Modify: `playground/src/api/index.ts`
- Create: `playground/src/views/system/log/list.vue`
- Create: `playground/src/views/system/log/data.ts`
- Modify: `playground/src/router/routes/modules/system.ts`

**Step 1: Write the failing page scaffold**

Reference intended APIs:

```ts
getLogList(...)
deleteLogs(...)
```

**Step 2: Run build to verify failure**

Run: `pnpm --dir playground build`
Expected: FAIL until APIs and page exist

**Step 3: Implement log API layer**

Support:

- list
- batch delete

**Step 4: Implement log page**

Include:

- filter form
- paginated table
- batch delete

Do not add create/edit functionality.

**Step 5: Add route**

Register `/system/log`

**Step 6: Run verification**

Run: `pnpm --dir playground build`
Expected: PASS

**Step 7: Commit**

```bash
git add playground/src/api/system/log.ts playground/src/api/system/index.ts playground/src/api/index.ts playground/src/views/system/log playground/src/router/routes/modules/system.ts
git commit -m "feat: add playground system log page"
```

### Task 6: Align route/menu/permission integration

**Files:**
- Modify: `playground/src/router/routes/modules/system.ts`
- Modify: affected `playground/src/views/system/*/data.ts`
- Modify: any access-code helper usage under `playground/src/store/` or page-level action definitions if needed
- Modify: locale files under `playground/src/locales/langs/zh-CN/` and related locales if required by the project standard

**Step 1: Wire route metadata for new pages**

Use titles/icons consistent with existing system routes.

**Step 2: Align action buttons with real access codes**

Apply access code checks to create/edit/delete/reset-password/detail-management buttons where the playground already supports this pattern.

**Step 3: Add locale keys**

Add labels for:

- jobs
- dictionaries
- dictionary details
- logs
- profile fields

**Step 4: Run verification**

Run: `pnpm --dir playground build`
Expected: PASS

**Step 5: Commit**

```bash
git add playground/src/router/routes/modules/system.ts playground/src/views/system playground/src/locales/langs
git commit -m "feat: align playground menus and permissions with backend"
```

### Task 7: Final verification

**Files:**
- Verify all files touched in previous tasks

**Step 1: Run frontend build**

Run: `pnpm --dir playground build`
Expected: BUILD SUCCESS

**Step 2: Run project lint or type check if available for playground**

Run: `pnpm --dir playground exec vue-tsc --noEmit`
Expected: PASS

**Step 3: Manual integration verification**

Verify these flows against the backend:

1. profile read/save
2. password change
3. job CRUD
4. dictionary CRUD
5. dictionary detail CRUD
6. system log list/delete
7. menu rendering for new pages
8. button visibility from backend access codes

**Step 4: Commit**

```bash
git add .
git commit -m "feat: complete playground admin ui for backend changes"
```
