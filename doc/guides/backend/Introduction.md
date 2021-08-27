# Introduction: Backend

The stack is designed to separate the different layers of the backend:

- service architecture (Ktor with Netty)
- the business logic (written by you)
- the persistence api (written by you, generated code uses Exposed)

This separation makes it possible to replace the service architecture
or the persistence API if you wish.

## Server.kt

[Server.kt](/core/core/src/jvmMain/kotlin/zakadabar/core/server/Server.kt) contains
the entry point of the backend application: the `main` function. 

You are free to extend this class and define your own main function as you wish.

`Server.kt` also contains a number of global variables and functions you can 
use to link the components of your application together.

`server` - is the actual server application instance, set by the `main` function

`module` - is a function to find a module between the known modules, this
is basically a very simplistic way of injection, see
[Find Other BLs](./BusinessLogic.md#Find-Other-BLs) for more information.

## Startup Sequence

1. `onConfigure`, this is a method to override if you extend the server
1. load settings, see [Settings](./Settings.md)
1. create SQL connection pool (HikariCP)
1. load modules, see [Modules](../common/Modules.md)
1. create missing tables and columns (Exposed)
1. resolve module dependencies
1. initialize DB (if necessary)
1. start modules, see [Modules](../common/Modules.md)
1. build the Ktor configuration
1. start the Ktor server

## Ktor

All Ktor related code of the core is in the `zakadabar.stack.backend.ktor` package.
In addition `lib:accounts` contains Ktor specific code for session handling.

At the end of server startup the `onBuildServer` function of the server creates
a [KtorServerBuilder](/core/core/src/jvmMain/kotlin/zakadabar/core/server/ktor/KtorServerBuilder.kt)
and calls its `build` function to build the Ktor instance.

### Add Ktor Plugins

There are two ways to add Ktor plugins to the server: 

- extend the `Server` and `KtorServerBuilder` classes,
- use the shorthand as described below.

To use the shorthand you have to add an import (IDEA cannot figure
out what to import automatically):

```kotlin
import zakadabar.stack.backend.ktor.*
```

Then add the plugins to `server`:

```kotlin
server += ContentNegotiation
```

```kotlin
server += ContentNegotiation {
    json(Json)
}
```

These result in an `install` call during Ktor configuration.