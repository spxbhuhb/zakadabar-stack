# Backend Routing

## Business Logic Routing

Business logic modules automatically add routing for CRUD. For actions and
queries you have to add a routing line to the BL routing:

```kotlin
class PrincipalBl : EntityBusinessLogicBase<PrincipalBo>(
    boClass = PrincipalBo::class
) {
    // ... code before ...

    override val router = router {
        action(PasswordChangeAction::class, ::action)
        query(PrincipalByEyeColor::class, ::query)
    }
    
    private fun action(executor: Executor, action: PasswordChangeAction) : ActionStatus {
        // ... execute the action ...
    }

    private fun query(executor: Executor, action: PrincipalByEyeColor) : List<PrincipalBo> {
        // ... execute the action ...
    }
}
```

## Special cases

For special cases you can access the Ktor routing logic directly. Have a look at 
[Server Routing](https://ktor.io/servers/features/routing.html).

All non-static backend routes:

* are under `/api`
* are authenticated (see [Accounts](/doc/guides/libraries/accounts/Introduction.md) for more information)

### Handle Non-BL

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