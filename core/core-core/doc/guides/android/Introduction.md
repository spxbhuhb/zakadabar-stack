# Introduction: Android

Android devices can run frontends and/or backends. The stack does not provide
UI components for Android.

Android frontends have access to:

* the shared data model
* communication adapters (out-of-the box, based on the data model)
* a JDBC driver for SQLite to use Exposed on Android
* backend components
    * database table definitions,
    * queries,
    * persistence APIs
    * business logics that use persistence APIs.

## Project Structure

* main project - IntelliJ IDEA - common, browser, jvm
* android project - Android Studio - android

The android project declares a dependency on the main project.

## Use the Data Models

When you declare a dependency on the main project, all data models in
`commonMain` will be automatically available in the android project.

You can use the same communication calls as on the browser frontend,
see the [example source code](../../../../lib/examples/src/jvmMain/kotlin/zakadabar/lib/examples/frontend/Main.kt)

## Exposed and SQLite

You can use the Exposed SQL library with the native SQLite database of Android.

Gradle dependency:

```kotlin
implementation("hu.simplexion.zakadabar:sqldroid:2021.7.16")
```

Initialization: 

```kotlin
        val jdbcUrl = "jdbc:sqldroid:/data/data/$packageName/databases/testdb.db"

        SQLDroidDriver.driverName = "SQLite JDBC"

        Database.registerJdbcDriver(
            "jdbc:sqldroid",
            SQLDroidDriver.className,
            SQLiteDialect.dialectName
        )

        Database.connect(jdbcUrl, SQLDroidDriver.className)

```


