# Next

This page contains the changes included in the next release.

This release contains a major addition: permission based authorization. There are no major breaking 
changes, applications that worked before should continue to work. Permissions are optional.

**NOTE** Changes with marker **very low** and such are technically breaking changes. However, they are
not major modifications and in most cases they should not break anything. Notes after the marker
are the places worth to check. If you extended them you may have to apply the change to your own class also.

## Core

**added**

- `authorizeByPermission` function to authorize execution by permission
- `PermissionBlProvider` interface for permission based authorization
- `SimplePermissionAuthorizer` authorizer for permission based authorization
- `appPermissions` variable for storing application permissions
- `AppPermissionsBase` class to for hard-coded permissions
- `with*Permission` functions for `ZkExecutor` and `ZkElement`

**changed**

- `Executor` now has two new properties: `permissionNames` and `permissionIds` **low**
- `Executor` now uses sets instead of lists **middle**
- `ZkExecutor` now has a new property: `permissions` **low**
- `ZkExecutor` now uses sets instead of lists **middle**
- `SessionBo` now uses sets instead of lists **middle**
- `ZkExecutor` now has a new property: `permissions`
- `StackSession` now has two new properties: `permissionNames` and `permissionIds` **low**
- `StackSession` now uses sets instead of lists **middle**
- ZkChip now use `text: nowrap` in CSS to prevent chip text wrapping **low**

## Lib: Accounts

**added**

- permissions, which can be used in conjunction with rules

**changed**

- add unique index to the account name in the account table