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
- `Logger.debug` to log debug messages
- `Slf4jLogger` wrapper around `org.slf4j.Logger`


changed:

- `ExposedPaTable.table` is now open
- `BoConstraint.type` to `constraintType` because of serialization conflict
- `modules` is now a `ModuleStore` and calls `onModuleLoad` when the module is added
- `StdOutLogger` now adds severity label
- 

## Lib: Blobs

added:

- `BlobBoCompanion<T,RT>.create(content : ByteArray, func: T.() -> Unit)` convenience function

## Lib: Email

New module for email sending.

## Cookbook

- module bundle recipe

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