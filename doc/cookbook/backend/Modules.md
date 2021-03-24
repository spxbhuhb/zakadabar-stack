# Backend Modules

Backend modules are packages that contain backend components. Those components usually extend one of

* [CustomBackend](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/CustomBackend.kt) see [Custom Backends](./CustomBackends.md)
* [RecordBackend](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/RecordBackend.kt) see [Record Backends](./RecordBackends.md)

Working example is in demo [Module.kt](../../../demo/src/jvmMain/kotlin/zakadabar/demo/backend/Module.kt)

## Write a Backend Module

Create an object that implements the interface [BackendModule](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/BackendModule.kt).

Use the `init` function to add the DTO backends this module contains:

```kotlin
@PublicApi
object Module : BackendModule {
    override fun init() {
        Server += SessionBackend
        Server += AccountBackend
        Server += RoleBackend
        Server += RoleGrantBackend

        Server += ShipBackend
        Server += SpeedBackend
    }
}
```

To perform cleanup during backend shutdown:

```kotlin
@PublicApi
object Module : BackendModule {
    override fun init() {
        // do something here
    }
    override fun cleanup() {
        TODO("cleanup")
    }
}
```

## Use a Backend Module

To include a backend module in your application add them to the configuration file (etc/zakadabar-server.yaml):

```yaml
modules:
  - Module
```

During start-up the stack:
- loads all modules from the configuration file,
- calls `init` for each module, in the order the configuration file lists the modules.