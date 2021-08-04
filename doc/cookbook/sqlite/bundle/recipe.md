# Database Transfer to Android

## Common

- [ExampleBo.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/sqlite/bundle/ExampleBo.kt)
- [ExampleBundle.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/sqlite/bundle/ExampleBundle.kt)

## JS

- [ExampleBundleCrud.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/sqlite/bundle/ExampleBundleCrud.kt)
- [ExampleCrud.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/sqlite/bundle/ExampleCrud.kt)

## JVM

- [ExampleBundleBl.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/sqlite/bundle/business/ExampleBundleBl.kt)
- [ExampleBl.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/sqlite/bundle/business/ExampleBl.kt)
- [ExampleBundlePa.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/sqlite/bundle/persistence/ExampleBundlePa.kt)
- [ExamplePa.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/sqlite/bundle/persistence/ExamplePa.kt)

## Android

On Android you have to download the data somehow. The example in `demo-sandbox-mobile` 
bends backwards to get the data from the server. Here is a bit clearer code:

```kotlin
val bundle = ExampleBundle.read(bundleId)
deployBundle(this.databasesPath, "test", bundle.content)
val db = Database.connectSqlite(this, "test")
```

I coded the activity belo really-really quick and really-really dirty. It works, but
it's a mess.

- [MainActivity.kt](/demo/demo-sandbox-mobile/src/main/java/zakadabar/demo/basic/android/MainActivity.kt)