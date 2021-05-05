# Backend Routing

For the backend Ktor supplies the routing logic. Have a look at 
[Server Routing](https://ktor.io/servers/features/routing.html).

All non-static backend routes:

* are under `/api`
* are authenticated (see [Accounts](../Accounts.md) for more information)

## Handle Non Crud, Non Query Routes

Handle these with [Custom Backends](./CustomBackends.md)

Have a look at [Server Routing](https://ktor.io/servers/features/routing.html) to get more information.

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

## Handle WebSocket Routes

Handle these with [Custom Backends](./CustomBackends.md)

For using WebSockets in Ktor check [WebSockets](https://ktor.io/servers/features/websockets.html).

```kotlin
object WebsocketBackend : CustomBackend() {

    override fun install(route: Route) {
        with(route) {
            webSocket("ws") { // available at /api/ws
                println("connection to ws")
            }
        }
    }

}
```