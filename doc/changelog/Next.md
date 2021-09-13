# Next

This page contains the changes included in the next release.

## Core

added:

- schema extension mechanism
- `envVar` schema extension to specify environment variable in settings explicitly
- explicit environment variable bindings for most server settings
- support of nested classes for automatic environment variable binding
- `--env-auto` server parameter for automatic BO mapping
- `--env-explicit` server parameter for explicit BO mapping
- `--no-db-schema-update` server parameter to disable automatic DB updates

changed:

- server environment variable parameters
- schema initializes `Instant` fields to `Clock.System.now()`
- schema initializes `LocalDate` fields to `Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date`
- schema initializes `LocalDateTime` fields to `Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())`
- environment variable initialization now handles nested classes
- `BoSchemaEntry` now has a second type parameter
- `BoSchemaEntry` declares fields:
    - kProperty
    - rules
    - extensions
    - defaultValue
- Clikt dependency to 3.2.0

removed:

- deprecated methods from schema classes
- `--ignore-environment` server parameter (replaced with `--env-auto` and `--env-explicit`)