# Next

This page contains the changes included in the next release.

## Breaking

- ZkPage and all variations are now stateless by default
- ZkArgPage: rename constructor parameter `cssClass` to `css`
- ZkArgPage: `args` is now non-null, `argsOrNull` may be null  
- authorizer, auditor, router, validator implementations has to handle null query and action returns
- ZkTable search is now case-insensitive
- ZkPageStyles.fixed now has `position: relative`

## Core

added:

- basic support for `LocalDate` and `LocalDateTime` data types
- actions and queries now can return with null result
- BO wrappers for primitive types for actions and queries
- auto-fetching select for entity references
- specify a query for the table directly
- `EntityId.read` function to read an entity directly from the id
- `newInstance` function for common code and actual implementations
- `matcher` property for ZkCustomColumn

changed:

- ZkPage and all variations are now stateless by default
- ZkArgPage constructor parameter `cssClass`  to `css`
- ZkArgPage `args` is now non-null, `argsOrNull` may be null
- ZkPageStyles.fixed now has `position: relative`
- ZkSelectBase hides arrow when in read-only mode
- ZkSelect2Base hides arrow when in read-only mode
- ZkTableStyles.tr uses pointer cursor
- ZkTable search is now case-insensitive

fixed:

- JDBC for Android: getObject now uses getLong without getInt
- `ZkCssStyleRule.gridGap` used `grid-column` instead of `grid-gap`

## Demos

changed:

- added `;AUTO_SERVER=TRUE` to H2 URLs

## Lib: Accounts

added:

- `Login.onSuccess` function to make customization easier
  
changes:

- `module.kt` (JS) - instances can be passed to `install` to override defaults
- `Login.kt` is now an open class

## Site

- cookbook is now a table

