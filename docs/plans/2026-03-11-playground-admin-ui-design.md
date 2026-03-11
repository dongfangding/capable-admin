# Playground Admin UI Design

**Goal:** Complete the `playground` admin frontend so it fully reflects the backend changes delivered in the capable-admin server, including profile management, jobs, dictionaries, logs, menus, and permission-driven UI behavior.

## Scope

This design is limited to the `playground` app inside the `vue-vben-admin` monorepo.

Included:

- profile page integration with real backend APIs
- system job management page
- system dictionary management page
- dictionary detail management inside the dictionary page
- system log page
- route and menu support for the new system pages
- frontend button visibility aligned with backend access codes

Excluded:

- `apps/web-*` implementations
- large-scale frontend refactoring
- login/logout page redesign
- backend changes

## Current State

The `playground` app already contains:

- system menu page
- system role page
- system dept page
- system user page
- a basic profile page using mock-like fields
- system route module with `/system/menu`, `/system/role`, `/system/dept`, `/system/user`
- API layer for `menu`, `role`, `dept`, `user`

The current gaps are:

- no job management page
- no dictionary page
- no log page
- profile page still uses non-backend fields like mock roles and introduction
- no frontend API modules for `job`, `dict`, `log`
- system route module does not include new backend-supported pages

## Recommended Approach

Use incremental completion on top of the existing `playground/src/views/system/*` pattern.

This is preferred because:

- current system pages already establish a consistent list/data/form structure
- API usage patterns are already in place
- new pages can match the existing table and drawer behavior
- risk stays low and backend alignment stays fast

## Information Architecture

### Routes

Add these routes in `playground/src/router/routes/modules/system.ts`:

- `/system/job`
- `/system/dict`
- `/system/log`

Do not add `dict-detail` as a first-level route.

### Dictionary detail interaction

Dictionary details should be managed from the dictionary page by opening a drawer or modal per dictionary row.

Reason:

- avoids menu fragmentation
- keeps parent-child relationship explicit
- matches admin usage patterns better than a separate page

## Page Design

### 1. Profile page

Refactor the existing profile page to use real backend APIs:

- read from `GET /user/profile`
- save base profile with `POST /user/profile`
- change password with `POST /user/change-password`

Base profile fields:

- nickname
- username (read-only)
- email
- mobile
- avatar
- sex

Password form:

- old password
- new password
- confirm new password on frontend

### 2. Job page

Create a standard CRUD page:

- table list
- search by name and enabled status if useful
- create/edit form drawer
- delete action

Reuse the same page composition style as existing `system/user`, `system/role`, and `system/dept`.

### 3. Dictionary page

Create a master-detail style page:

- dictionary list table
- create/edit dictionary form
- delete dictionary action
- row action to open dictionary detail management

### 4. Dictionary detail modal/drawer

Within the dictionary page:

- list detail rows for one selected dictionary
- create/edit detail entries
- delete detail entries

Fields:

- label
- value
- dictSort

### 5. System log page

Create a read-heavy page with:

- filter form
- paginated table
- batch delete

No create/edit actions are needed.

Main filters:

- username
- logType
- method
- time range

## API Layer

### New API modules

Add under `playground/src/api/system/`:

- `job.ts`
- `dict.ts`
- `log.ts`

Export them through:

- `playground/src/api/system/index.ts`
- `playground/src/api/index.ts`

### Profile API changes

Extend `playground/src/api/core/user.ts` with:

- `getUserProfileApi`
- `updateUserProfileApi`
- `changePasswordApi`

Keep existing `getUserInfoApi()` because it still supports layout/user session information.

## Permissions and Menus

### Route component registration

The frontend should provide actual route components for:

- `/system/job/list`
- `/system/dict/list`
- `/system/log/list`

This allows backend menu-driven routing to resolve the new pages correctly.

### Button visibility

Page-level buttons and row actions should be tied to real access codes returned by the backend.

Expected examples:

- create
- edit
- delete
- enable/disable
- reset password
- dictionary detail management

### Navigation behavior

Do not hard-force sidebar menu entries if the backend does not return them.

The frontend should:

- support the routes
- render them when backend menu data includes them
- hide them when backend menu data omits them

## Error Handling

- save failures keep current form state
- delete failures show message feedback and keep table state unchanged
- profile base info and password form errors are isolated from each other
- dictionary detail modal should refresh or close safely if the parent dictionary is removed

## Validation and UX Rules

- profile username remains read-only
- password confirmation is validated on frontend before submit
- table pages should show normal empty states
- destructive actions use the same confirmation patterns already used in existing pages

## Verification Strategy

### Frontend verification

- type check or build must pass
- pages render without missing route/component errors

### Functional verification

- profile read/save works
- password change works
- job CRUD works
- dictionary CRUD works
- dictionary detail CRUD works
- system log list/delete works

### Permission verification

- backend-provided menu items render in sidebar
- missing menu items do not render
- unauthorized buttons are hidden

## Delivery Order

1. API layer and profile integration
2. job page
3. dictionary page and detail modal
4. log page
5. route/menu/permission alignment
