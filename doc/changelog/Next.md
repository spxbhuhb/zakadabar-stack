# Next

This page contains the changes included in the next release.

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