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

## Business Objects

When you declare a dependency on the main project, all business object
definitions in `commonMain` are automatically available in the android project.

You can use the same communication calls as on the browser frontend,
see the [example source code](/lib/examples/src/jvmMain/kotlin/zakadabar/lib/examples/frontend/Main.kt)

## Exposed and SQLite

You can use the Exposed SQL library with the native SQLite database of Android.

Gradle dependency:

```kotlin
implementation("hu.simplexion.zakadabar:core-android:$stackVersion")
```

If your Android project depends on another project that uses SQLite (a server project for
example), you may have to exclude the serial driver from the dependency.

```kotlin
implementation(project(":demo:demo-sandbox")) {
    exclude(group="org.xerial", module="sqlite-jdbc")
}
```

Initialization: 

```kotlin
Database.connectSqlite(this, "test")
```

After initialization, the usual Exposed functions are available.

Also, business logics and persistence apis are available from the
main project. 

```kotlin
transaction {
    SchemaUtils.createMissingTablesAndColumns(
        AccountPrivateExposedTableCommon,
        TranslationExposedTableGen,
        LocaleExposedTableGen,
        DemoExposedTableGen,
        DemoBlobExposedPaTable
    )
}

val pa = DemoExposedPaGen()

val created = pa.withTransaction {
    pa.create(default {
        name = "Hello"
        value = 12
    })
}

val readBack = pa.withTransaction {
    pa.read(created.id)
}
```

### Database Inspector

The database inspector shows the database closed by default. To keep it open,
you can add this line to your main activity:

**Use this for DEBUG only, not for production!**

```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var connection: Connection // Only for DEBUG

    override fun onCreate(savedInstanceState: Bundle?) {
        val db = Database.connectSqlite(this, "test")
        connection = DriverManager.getConnection(db.url)
    }
}
```

### Database Transfer

For some use cases it is very convenient to build a database on the backend
and download it to the frontend in one request. Table definitions can be
shared, so it is possible to define most database related operations in
the main project and use them in client code.

For a working example check the Database Transfer cookbook recipe.