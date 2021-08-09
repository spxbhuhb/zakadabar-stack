# Custom Backends

* are fully customizable request handlers,
* are Kotlin objects that extend [RoutedModule](/core/core/src/commonMain/kotlin/zakadabar/stack/backend/RoutedModule.kt),
* **are not** added automatically (see: [Modules](../common/Modules.md))

Examples:

* [PingBackend](/lib/examples/src/jvmMain/kotlin/zakadabar/lib/examples/backend/misc/PingBackend.kt)
* [WebsocketBackend](/lib/examples/src/jvmMain/kotlin/zakadabar/lib/examples/backend/misc/WebsocketBackend.kt)

```kotlin
object PingBackend : RoutedModule {

    override fun onInstallRoutes(route: Any) {
        with(route as Route) {
            get("ping") {
                call.respond("pong")
            }
        }
    }
}
```