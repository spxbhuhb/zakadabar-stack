# Custom Backends

* are fully customizable request handlers,
* are Kotlin objects that extend [RoutedModule](/core/core-core/src/jvmMain/kotlin/zakadabar/stack/backend/RoutedModule.kt),
* **are not** added automatically.

Examples:

* [PingBackend](/lib/examples/src/jvmMain/kotlin/zakadabar/lib/examples/backend/misc/PingBackend.kt)
* [WebsocketBackend](/lib/examples/src/jvmMain/kotlin/zakadabar/lib/examples/backend/misc/WebsocketBackend.kt)

```kotlin
object PingBackend : RoutedModule {

    override fun install(route: Any) {
        with(route as Route) {
            get("ping") {
                call.respond("pong")
            }
        }
    }
}
```

## Add Custom Backend From Configuration

You may add custom backends directly from the configuration, without creating a module:

```
modules:
  - zakadabar.demo.lib.backend.lib.PingBackend
```

To add a custom backend from a module see: [Modules](./Modules.md)

## Add Custom Backend Programmatically

You can add a backend module programmatically by adding it
to [Server](/core/core-core/src/jvmMain/kotlin/zakadabar/stack/backend/Server.kt)

The Server calls the `onModuleLoad` method of CustomBackend when it is added.

```kotlin
server += PingBackend
```
