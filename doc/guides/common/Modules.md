# Modules

Modules are the building blocks of the application. When you decide the functions
your application provides, you make a list the modules you need for those functions.

Once you have the list, you can set up your application by:

- adding the modules programmatically, and/or
- adding the modules from the configuration (JVM).

You can have modules on any node, frontend and backend both works. This makes
it possible to use common code.

## Add Programmatically

To add modules programmatically, use the `modules` global variable and simply
add the modules like this.

```kotlin
modules += SimpleExampleBl()
```

## Add From Configuration (JVM)

Add the module to the configuration file (etc/stack.server.yaml) as
the example below shows.

```
modules:
  - zakadabar.site.backend.Module
  - zakadabar.site.backend.Module2
```

## Module Bundles

Module bundles pack other modules together. This makes installation and configuration
easier.

Most libraries provide a module bundle class, so you can add the library to the application
easily, like this:

```kotlin
modules += ExampleModuleBundle()
```

To write a module bundle:

- Create an object that implements the interface [CommonModule](/core/core/src/commonMain/kotlin/zakadabar/stack/module/CommonModule.kt).
- Use the `onModuleLoad` function to add modules to the server.

```kotlin
@PublicApi
object Module : CommonModule {
    
    override fun onModuleLoad() {
        modules += Example1Bl()
        modules += Example2Bl()
    }
    
}
```

## Module Lifecycle

This picture summarizes the module startup sequence.

![Module Startup](module-init.png)

### onModuleLoad

Perform only basic initialization in `onModuleLoad`.

### onInitializeDb

First time DB initialization and DB upgrades (if necessary).

### onModuleStart

Called after:

- loaded all modules with `onModuleLoad` executed
- resolved all module dependencies
- initialized the database: `onInitializeDb` executed for all modules
- settings are available (when SettingBl is added)

### onInstallRoutes

**[RoutedModule] only**

This function adds the routes to Ktor. Most backends delegate setting
the routes to their `router`. See [Routing](../backend/Routing.md) for more information.

### onInstallStatic

**[RoutedModule] only**

Add static resources to Ktor. It provides an easy way to register directories for static service. Check
[ContentBackend](/core/core/src/jvmMain/kotlin/zakadabar/stack/backend/custom/ContentBackend.kt)
for an example.

