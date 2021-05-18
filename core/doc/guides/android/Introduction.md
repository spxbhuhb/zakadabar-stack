# Introduction: Android Frontend

As of now Android projects built with the stack can use these features:

* use the shared data model,
* have communication instances out-of-the box, based on the model.

We plan to provide better integration and pre-built components for the Android frontend
in the future.

## Project Structure

* main project - IntelliJ IDEA - common, browser frontend, jvm backend
* android project - Android Studio - android frontend

The android project declares a dependency on the main project.

## Use the Data Models

When you declare a dependency on the main project, all data models in
`commonMain` will be automatically available in the android project.

You can use the same communication calls as on the browser frontend,
see the [example source code](../../../../lib/examples/src/jvmMain/kotlin/zakadabar/lib/examples/frontend/Main.kt)
