Bender is a simple tool for generating DTO classes for Zakadabar.

Usage:

1. fill the fields above, 
1. click on the generate button - the source codes will be displayed below,
1. click on the copy buttons (above) or the copy icons (below, top-right corner of code blocks),
1. paste what's on the clipboard to the appropriate directory in IDEA.

Bender generates these codes:

* common: data model and schema,
* browser: table, form, crud,
* backend: record backend, DAO, Exposed table.

Bender is **experimental**: we hope that it works.

Supported types (case-insensitive):

* Boolean
* Double
* Enum
* Instant
* Int
* Long
* RecordId  
* String
* UUID

Bender preforms all code generation on client side, your data model does not go
out of your computer.

## Common

Copy this into the directory `commonMain/kotlin/@packageName@/data`:

```kotlin
// commonSource
```

## Browser Frontend

Copy this into the directory `jsMain/kotlin/@packageName@/frontend/pages`:

```kotlin
// browserSource
```

## JVM Backend

Copy this into the directory `jsMain/kotlin/@packageName@/backend/record`:

```kotlin
// backendSource
```