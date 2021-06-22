# Next

This page contains the changes included in the next release.

## Core

added: 

- SimpleRoleAuthorizer now has a `PUBLIC` keyword to provide public access, see [Authorizer](/doc/guides/backend/Authorizer.md)
- TestCompanionBase helper class for unit tests

changed:

- Backend testing guide to use provided base classes

## Lib: Accounts

added:

- AuthTestCompanionBase helper class for authorized unit tests

fixed:

- Additional roles are added only when the database is empty #48

## Lib: Blobs

added:

- upload and download convenience functions
- documentation
- unit tests

deprecated:

- `listByReference` - use `byReference` instead
