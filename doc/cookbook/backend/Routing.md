# Backend Routing

For the backend Ktor supplies the routing logic. Have a look at 
[Server Routing](https://ktor.io/servers/features/routing.html).

All non-static backend routes:

* are under `/api/<shid>/`
* are authenticated (see [Accounts](../common/Accounts.md) for more information) 

## Add Non Crud, Non Query Routes

To route non-CRUD and non-Query requests add a Ktor route like this.

Have a look at [Server Routing](https://ktor.io/servers/features/routing.html) to get more
information.

```kotlin
 override fun install(route: Route) {

    // this routing serves:
    //
    //   GET /api/<shid>/statistics

    with(route) {
        get("${shid}/statistics") { 
            call.respondText("some statistics") 
        }
    }

}
```

## Add WebSocket Routing

For using WebSockets in Ktor check [WebSockets](https://ktor.io/servers/features/websockets.html).

To route them in Zakadabar (in this case mixed with a rest interface) as the example shows.

```kotlin
 override fun install(route: Route) {

    // this routing serves:
    //
    //   WS /api/<shid>/yourData/{id}/feed

    with(route) {
        webSocket("${shid}/yourData/{id}/feed") { 
           println("connection to ws")
        }
    }
}
```