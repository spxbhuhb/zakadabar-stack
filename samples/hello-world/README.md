This is an example that shows the working frontend and backend together.

**Shortcuts**

* [index.html](src/jsMain/resources/index.html)
* [main.kt](src/jsMain/kotlin/main.kt)
* [Module.kt](src/jsMain/kotlin/zakadabar/samples/helloworld/frontend/Module.kt)
* [HelloWorldCenter.kt](src/jsMain/kotlin/zakadabar/samples/helloworld/frontend/HelloWorldCenter.kt)
* [Welcome.kt](src/jsMain/kotlin/zakadabar/samples/helloworld/frontend/Welcome.kt)

**Setup**

You need an SQL database to run this example (no need for tables, the program will create them).
To configure the database parameters, edit [zakadabar-server.yml](etc/zakadabar-server.yaml).

**Running the Example**

To run it, execute these two Gradle tasks. They won't finish, they just keep running until you
stop them manually.

* 01-beginner / hello-world / Tasks / application / run
* 01-beginner / hello-world / Tasks / kotlin browser / jsBrowserRun

The first is the server application that runs in a JVM, the second is the WebPack development server.

When they are up and running a web browser opens with the URL: http://localhost:3000

This is where you can see the Hello World application.

You can make the WebPack server refreshing the page automatically by adding "--continuous"
to the program arguments of the IDEA Run Configuration.

**Don't forget to stop other examples before you start a next one.**