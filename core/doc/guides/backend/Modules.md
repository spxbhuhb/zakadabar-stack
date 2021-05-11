# Backend Modules

Backend modules are packages that contain backend components. Those components usually extend one of

* [CustomBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/custom/CustomBackend.kt)
  see [Custom Backends](./CustomBackends.md)
* [RecordBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/record/RecordBackend.kt)
  see [Record Backends](./RecordBackends.md)

When supporting actions and queries backends implement one or both of these interfaces:

* [ActionBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/action/ActionBackend.kt)
* [QueryBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/data/query/QueryBackend.kt)

## Write a Backend Module

Create an object that implements the
interface [BackendModule](/src/jvmMain/kotlin/zakadabar/stack/backend/BackendModule.kt).

Use the `onModuleLoad` function to add backends this module contains:

```kotlin
@PublicApi
object Module : BackendModule {
    
    override fun onModuleLoad() {
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
    
    override fun onModuleLoad() {
        // do something here
    }
    
    override fun onModuleStop() {
        // do the cleanup here
    }
    
}
```

## Use a Backend Module

To include a backend module in your application add them to the configuration file (etc/zakadabar.stack.server.yaml):

```yaml
modules:
  - zakadabar.site.backend.Module
```

During start-up the stack:

* loads all modules from the configuration file,
* calls `onModuleLoad` for each module, in the order the configuration file lists the modules,
* calls `onModuleStart` for each module, in the order the configuration file lists the modules,
* calls `installRoutes` for each module, in the order the configuration file lists the modules,
* calls `installStatic` for each module, in the order the configuration file lists the modules,
