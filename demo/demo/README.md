This demo project shows how to use the stack and contains quite some documentation about what's going on.

## Running

To run the demo:

* download the stack repository
* create a database and set parameters
  in [template/app/etc/zakadabar-server.yaml](template/app/etc/zakadabar-server.yaml)
* run the `application/run` Gradle task
* run the `kotlin browser/jsBrowserRun` Gradle task

After a while your favourite web browser should open and display the home page of the demo.

## Application Structure

In a typical application you have:

* a database
* a backend that runs on JVM (Java Virtual Machine)
* a web frontend that is an SPA (Single Page Application)
* mobile frontends that are Android / iOS applications

As of now the stack covers the first two. (We plan to integrate it with mobile somehow, but we are not there yet.)

In a large scale application there is load balancing, resilience and so on, but we won't go into that.

The stack and the applications built on it use Kotlin/Multiplatform with three code sets:

* [commonMain](#commonMain) - code shared between the backend and the frontends
* [jvmMain](#jvmMain) - the backend JVM application
* [jsMain](#jsMain) - the web SPA application

There are a few other things in this directory:

* [etc](#etc)
* [Gradle Build Script](#Gradle-Build-Script)
* [Webpack Config](#Webpack-Config)

## commonMain

Contains code we use both on the backend and on frontends.

When you change something in this code set, it changes **everywhere**.

Most important parts of the common code are the DTOs (Data Transfer Objects).

DTOs are data classes sent by the backend to the frontend and vice-versa. You may think DTOs as definitions of the frontend - backend API.

You can see an example of a DTO below, there are a few important points about this:

* **BOTH the frontend and the backend uses this very same piece of code, from the same file**
* communication is ready to go, no need to write anything else
* this DTO has a schema, useful for
  * filling an instance with default values
  * automatic validation of forms on the frontend
  * automatic validation of incoming data on the server
    * all these above is either a one-liner or already provided by the stack
* also, this DTO defines two queries, which are
    * automatically serialized / deserialized
    * are type-safe
    * easy to handle on both sides

```kotlin
@Serializable
data class ShipDto(

    override var id: RecordId<ShipDto>,
    var name: String,
    var speed: RecordId<SpeedDto>

) : RecordDto<ShipDto> {

    companion object : RecordDtoCompanion<ShipDto>({
        recordType = "ship"
    })

    override fun getRecordType() = recordType
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name max 20 min 2 notEquals "Titanic"
        + ::speed
    }

}
```

The strict definition and code sharing (between frontend and backend) of communicated objects have drastic consequences.
Just an example: it happened that I wanted to rename a query DTO. I used the refactor function: the name of the class
changed both on the frontend and on the backend and my program was still working.

Check out [commonMain](src/commonMain/kotlin/zakadabar/demo/data/README.md) for more information.

## jvmMain

Code for the backend. Based on Ktor, contains:

* interface for the frontends (HTTP/HTTPS)
* persistence (saving data into SQL)
* queries (loading data from SQL)
* authentication and authorization
* business logic
* backend services
* M2M (machine to machine) interfaces

Check out [jvmMain](src/jvmMain) for more information.

## jsMain

Code for the web browser frontend.

When the user opens the page in the browser [index.html](src/jsMain/resources/index.html) loads first.

This is rather short and mostly it just loads the application's `.js` file and calls `main`
from [main.kt](src/jsMain/kotlin/main.kt).

Check out [jsMain](src/jsMain) for more information.

## etc

This directory contains the configuration for the backend.

There are a few important points here:

* database connection
* place of static resource files to serve for the frontend
* modules to load

```yaml
database:
  driverClassName: org.postgresql.Driver
  jdbcUrl: jdbc:postgresql://localhost:5432/zakadabar
  username: test
  password: Almafa.12

traceRouting: false
staticResources: ./var/static

modules:
  - Module
```

## Gradle Build Script

Not much going on here as the demo is in the same repository as the core, thus the gradle scrip is quite simple.

Check out the [Application Template](https://github.com/spxbhuhb/zakadabar-application-template) for a gradle script that uses the stack as an imported module.

## Webpack Config

The `webpack.config.d` directory contains configuration files for webpack. As of now it only defines the web environment, but it may contain anything to set up webpack properly.

Check [Configuring webpack bundling](https://kotlinlang.org/docs/reference/js-project-setup.html#configuring-webpack-bundling)
in the multiplatform documentation.