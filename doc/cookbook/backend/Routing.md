# Backend Routing

For the backend Ktor supplies the routing logic. Have a look at 
[Server Routing](https://ktor.io/servers/features/routing.html).

All non-static backend routes are:

* under `/api/<shid>/`
* authenticated (see [Accounts](../common/Accounts.md) for more information) 

## Add REST Routing for a Record DTO

To route REST requests for an entity type use the `recordRestApi` helper function from 
[restApi.kt](../../../src/jvmMain/kotlin/zakadabar/stack/backend/extend/restApi.kt)
in the [Module](Modules.md) file.

```kotlin
 override fun install(route: Route) {

    // this routing serves (data-name is the part after "/" in the DTO type):
    //
    //   GET    /api/<shid>/<data-name>
    //   GET    /api/<shid>/<data-name>/<id>
    //   GET    /api/<shid>/<data-name>?parent=<parent-id>
    //   POST   /api/<shid>/<data-name>
    //   PATCH  /api/<shid>/<data-name>
     
    recordRestApi(route, Backend, YourData::class, YourData.type) 

}
```

## Add REST Routing for an Entity DTO

To route REST requests for an entity type use the `entityRestApi` helper function from 
[restApi.kt](../../../src/jvmMain/kotlin/zakadabar/stack/backend/extend/restApi.kt)
in the [Module](Modules.md) file.

```kotlin
 override fun install(route: Route) {

    // this routing serves (data-name is the part after "/" in the DTO type):
    //
    //   GET    /api/<shid>/<data-name>
    //   GET    /api/<shid>/<data-name>/<id>
    //   GET    /api/<shid>/<data-name>?parent=<parent-id>
    //   POST   /api/<shid>/<data-name>
    //   PATCH  /api/<shid>/<data-name>
     
    entityRestApi(route, Backend, YourData::class, YourData.type) 

}
```

## Add Non REST Routing

To route non-rest requests add a Ktor route like this. You may mix REST and non-REST routing.

Have a look at [Server Routing](https://ktor.io/servers/features/routing.html) to get more
information.

```kotlin
 override fun install(route: Route) {

    restApi(route, Backend, YourData::class, YourData.type)

    // this routing serves:
    //
    //   GET /api/<shid>/statistics

    with(route) {
        get("/statistics") { 
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

        restApi(route, Backend, YourDynamicData::class, YourDynamicData.type)

        // this routing serves:
        //
        //   WS /api/<shid>/yourData/{id}/feed

        with(route) {
            webSocket("/yourData/{id}/feed") { 
               println("connection to ws")
            }
        }
    }
```