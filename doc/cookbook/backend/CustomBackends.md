# Custom Backends

* are fully customizable request handlers,
* are Kotlin objects that extend [CustomBackend](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/CustomBackend.kt),
* **are not** added automatically.

Examples:

* [PingBackend](../../../demo/src/jvmMain/kotlin/zakadabar/demo/backend/misc/PingBackend.kt)
* [SessionBackend](../../../demo/src/jvmMain/kotlin/zakadabar/demo/backend/account/session/SessionBackend.kt)

```kotlin
object PingBackend : CustomBackend() {

    override fun install(route: Route) {
        with(route) {
            get("ping") {
                call.respond("pong")
            }
        }
    }
}
```

## Add Custom Backend From Configuration

You may add custom backends directly from the configuration, without creating a module:

```yaml
modules:
  - zakadabar.demo.backend.misc.PingBackend
```

To add a custom backend from a module see: [Modules](./Modules.md)

## Add Custom Backend Programmatically

You can add a backend module programmatically by adding it to [Server](../../../core/src/jvmMain/kotlin/zakadabar/stack/backend/Server.kt)

The Server calls the `init` method of CustomBackend when it is added.

```kotlin
Server += PingBackend
```
