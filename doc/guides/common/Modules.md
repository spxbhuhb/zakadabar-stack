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
- Use the `onModuleLoad` function to add modules that are part of the bundle.

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

This picture summarizes the module startup sequence when running 

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

### onInstallRoutes (RoutedModule only)

This function adds the routes to Ktor. Most backends delegate setting
the routes to their `router`. See [Routing](../backend/Routing.md) for more information.

### onInstallStatic (RoutedModule only)

Add static resources to Ktor. It provides an easy way to register directories for static service. Check
[ContentBackend](/core/core/src/jvmMain/kotlin/zakadabar/stack/backend/custom/ContentBackend.kt)
for an example.

## Internals

### Module Store

The application uses [ModuleStore](/core/core/src/commonMain/kotlin/zakadabar/stack/module/ModuleStore.kt)
instances to store modules.

The default module store is created automatically and assigned to the global variable `modules`.

Most applications does not need another module store, but it is possible to create, start, stop, etc.
more.

### Module Startup Buckets

[ModuleStartupBuckets](/core/core/src/commonMain/kotlin/zakadabar/stack/module/ModuleStartupBucket.kt) define 
the order in which modules start and stop.
[ModuleStore](/core/core/src/commonMain/kotlin/zakadabar/stack/module/ModuleStore.kt)
contains the list of buckets in the `buckets` property.

Buckets are ordered by their `order` property: lower the order, earlier the modules 
in that bucket start.

There are pre-defined buckets, started in this order:

- `BOOTSTRAP` modules needed for other modules to load properly, SQL providers for example
- `DEFAULT` normal business level modules, this is the default bucket
- `PUBLISH` modules that accept incoming connections, Ktor for example
- `STEADY` modules to be started after the system becomes steady

When you add a module to a store, the store goes over the buckets (from lowest oder
to highest) and calls the `selector` function of each bucket. If the selector
returns with `true` the module is added to the given bucket.

The `DEFAULT` bucket is an exception, it is excluded from the `selector` mechanism.
The store adds a module to the `DEFAULT` bucket when the selector all other buckets
returned with `false`.

You can specify one of the pre-defined buckets for a module by adding one of
the bucket selecting interfaces:

```kotlin
class ExampleBootstrapModule : CommonModule, BootstrapBucket
class ExamplePublishModule : CommonModule, PublishBucket
class ExampleSteadyModule : CommonModule, SteadyBucket
```

To add a new bucket, simply add the instance to the module store as below:

```kotlin
modules += ModuleStartupBucket(100) { it is ExampleBucketInterface }
```

