# Backend Modules

Backend modules are packages that contain backend components.

Working example:
  - [The Old Man from Scene 24 - Module.kt](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/the-old-man-from-Scene-24/src/jsMain/kotlin/zakadabar/samples/holygrail/backend/Module.kt)

## Writing a Backend Module

Create an object that implements the interface [BackendModule](../../../src/jvmMain/kotlin/zakadabar/stack/backend/BackendModule.kt).

Use the `init` function to add the DTO backends this module contains:

```kotlin
@PublicApi
object RabbitModule : BackendModule {
    override fun init() {
        BackendContext += RabbitBackend
    }
}
```

To perform cleanup during backend shutdown:

```kotlin
@PublicApi
object RabbitModule : BackendModule {
    override fun init() {
        BackendContext += RabbitBackend
    }
    override fun cleanup() {
        TODO("cleanup")
    }
}
```

## Using a Backend Module

To include a backend module in your application add them to the configuration file (etc/zakadabar-server.yaml):

```yaml
modules:
  - zakadabar.samples.holygrail.backend.Module
```

During start-up the stack:
- loads all modules from the configuration file,
- calls `init` for each module, in the order the configuration file lists the modules.