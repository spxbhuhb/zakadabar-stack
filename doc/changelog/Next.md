# Next

This page contains the changes included in the next release.

## Core

added:

- merge environment variables into settings
- `--ignore-environment` server option to ignore environment variables
- `critical` function to provide a multiplatform synchronization primitive
- `BoSchemaEntry<T>.decodeFromText` 
- `BoSchemaEntry<T>.setFromText`
- `EntityBoCompanion<T>.create(func: T.() -> Unit)` convenience function

changed:

- `ExposedPaTable.table` is now open

## Bender

changed:

- replace `translate` with `localized` to avoid deprecation warning
- remove `data` from BO package name
- remove `backend` from BL, PA package names  
- replace package name `frontend.pages` with `browser` in generated browser code
- table name pattern is now `${baseName}Table` instead of `${baseName}ExposedTableGen`
- remove `internal` from table columns
- table is now a class by default, table name is constructor parameter
- pa name pattern is now `${baseName}Pa` instead of `${baseName}ExposedPaGen`
- remove generated comments

fixed:

- authorizer example in the documentation