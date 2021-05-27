# Backend Modules

Backend modules are the building block of the server. When you decide the functions
your application provides, you make a list the modules you need for those functions.

Once you have the list, you can set up your application by:

- adding the modules from programmatically, and/or
- adding the modules from the configuration.

## Add Modules From Configuration

Add the module to the configuration file (etc/zakadabar.stack.server.yaml) as
the example below shows. You can list as many modules as you want.

```
modules:
  - zakadabar.site.backend.Module
```

## Add Modules Programmatically

To add modules programmatically, use the `server` global variable and simply
add the modules like this.

```kotlin
server += SimpleExampleBl()
```

You can create a module that adds



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


During start-up the stack:

* loads all modules from the configuration file,
* calls `onModuleLoad` for each module, in the order the configuration file lists the modules,
* calls `onModuleStart` for each module, in the order the configuration file lists the modules,
* calls `installRoutes` for each module, in the order the configuration file lists the modules,
* calls `installStatic` for each module, in the order the configuration file lists the modules,
