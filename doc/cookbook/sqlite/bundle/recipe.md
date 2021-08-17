# Database Transfer to Android

```yaml
level: Advanced
targets:
  - android
  - jvm
tags:
  - android
  - mobile
  - backend
  - frontend
  - database
  - sqlite
```

This recipe shows how to build an SQLite database bundle on a server mode and transfer it to
an Android client.

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

## Guides

- [Introduction: Android](/doc/guides/android/Introduction.md)

## Code

- [ExampleBo.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/entity/builtin/ExampleBo.kt)
- [ExampleBundle.kt](/cookbook/src/commonMain/kotlin/zakadabar/cookbook/sqlite/bundle/ExampleBundle.kt)
- [ExampleBundleCrud.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/sqlite/bundle/ExampleBundleCrud.kt)
- [ExampleCrud.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/entity/builtin/ExampleBoCrud.kt)
- [ExampleBundleBl.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/sqlite/bundle/business/ExampleBundleBl.kt)
- [ExampleBl.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/entity/builtin/ExampleBl.kt)
- [ExampleBundlePa.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/sqlite/bundle/persistence/ExampleBundlePa.kt)
- [ExamplePa.kt](/cookbook/src/jvmMain/kotlin/zakadabar/cookbook/entity/builtin/ExamplePa.kt)
