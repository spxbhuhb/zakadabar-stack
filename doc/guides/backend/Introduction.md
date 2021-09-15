# Introduction: Backend

Zakadabar is designed to separate different layers of the server infrastructure:

- service architecture (Ktor with Netty)
- the business logic (written by you)
- the persistence api (written by you)

This separation makes it possible to replace the service architecture
or the persistence API if you wish.

## Server.kt

[Server.kt](/core/core/src/jvmMain/kotlin/zakadabar/core/server/Server.kt) contains
the entry point of the server applications: the `main` function. 

You are free to extend this class and define your own main function as you wish.

`Server.kt` also contains a number of global variables and functions you can 
use to link the components of your application together.

`server` - is the actual server application instance, set by the `main` function

`module` - is a function to find a module between the known modules, this
is basically a very simplistic way of injection, see
[Find Other BLs](./BusinessLogic.md#Find-Other-BLs) for more information.

### Command Line Parameters

When started from the command line, `Server.kt` accepts the following parameters:

#### Settings

`--settings <settings-file>`

Paths to the server settings file. See [Settings](./Settings.md) for more information.
Default is "./stack.server.yaml".

#### Start Until

`--start-until <server-state>`

Start the server up until a given state (inclusive). With this parameter it is possible
to stop server startup at a given point.

States:

- `settings-load`
- `connect-db`
- `module-load`
- `initialize-db`
- `module-start`
- `complete` (accepting connections)

#### No DB Schema Update

`--no-db-schema-update`

When this flag is present, the server won't perform DB schema update at startup.

#### Env Auto

`--env-auto`

When this flag is present, the server will perform automatic environment variable
to setting mapping, see [Settings](./Settings.md) for details.

#### Env Explicit

`--env-explicit`

When this flag is present, the server will perform explicit environment variable
to setting mapping, see [Settings](./Settings.md) for details.

### Settings By Environment Variables

When the `--env-explicit` flag is present, the server merges the following environment
variables into server settings (see [Settings](./Settings.md) for details).

- ZK_SERVER_NAME
- ZK_SERVER_LOCALE
- ZK_STATIC_RESOURCES  
- ZK_DB_DRIVER
- ZK_DB_JDBC_URL
- ZK_DB_USERNAME
- ZK_DB_PASSWORD
- ZK_DB_DEBUG
- ZK_KTOR_PORT

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