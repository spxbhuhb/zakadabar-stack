# Backend Modules

Backend modules are packages that contain other backend components:

- [Business Logic](./BusinessLogic.md) components,
- [Custom Backend](./CustomBackends.md) components.

## Write a Backend Module

Create an object that implements the
interface [BackendModule](/src/jvmMain/kotlin/zakadabar/stack/backend/BackendModule.kt).

Use the `onModuleLoad` function to add components this module contains:

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

```
modules:
  - zakadabar.site.backend.Module
```

During start-up the stack:

* loads all modules from the configuration file,
* calls `onModuleLoad` for each module, in the order the configuration file lists the modules,
* calls `onModuleStart` for each module, in the order the configuration file lists the modules,
* calls `installRoutes` for each module, in the order the configuration file lists the modules,
* calls `installStatic` for each module, in the order the configuration file lists the modules,
