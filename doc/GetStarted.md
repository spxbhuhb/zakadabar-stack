# Get Started

## Introduction

Zakadabar Stack is about sensible defaults. While you can replace the whole frontend, the whole backend,
or both (which makes little sense, but actually it does make sense), the point of sensible defaults 
is that in most cases we don't want to replace them.

The structure of our default application:

* The frontend stands on its own, we don't use third party libraries such as react.
    * The stack provides UI components for the browser frontend.
* The backend uses [Ktor](https://ktor.io) as a web server.
    * It is configured automatically, you don't have to know Ktor to use the stack.
* The built-in backend modules use [Exposed](https://github.com/JetBrains/Exposed)
  with [HikariCP](https://github.com/brettwooldridge/HikariCP) and an SQL database.
    * You can ignore the default modules if you want (for example to use a non-SQL database).

The stack is a Kotlin Multiplatform Project and is intended to be used by Kotlin Multiplatform Projects.

## The Application Template

The best way to start using the stack is to create your own project from
the [Application Template](https://github.com/spxbhuhb/zakadabar-application-template).

Use GitHub's `Use this template` function to create your own repository that is a copy
of the application template and then follow the steps in the README.

The template is a fully working application which you can customize by setting the project name and running one gradle
task (see README.md for details).

After customization, you can run another task and have a Zip file and/or Docker images ready for deployment.

## Bender

When you have a working application, you can start adding functions to it. The fastest way
to add new functions is to use the [Bender](./guides/tools/Bender.md).

Bender is a little tool which lets you define your data model and generate working, ready-to-go
frontend and backend code from the model.

## Reading On

We suggest that you have a quick look at the topics below to get an idea what's going on.

- [Introduction: Software Model](./guides/Introduction.md)
- [Introduction: Backend](./guides/backend/Introduction.md)  
- [Introduction: Browser](./guides/browser/Introduction.md)
- [Introduction: Android](./guides/android/Introduction.md)
- [Introduction: Plug And Play](./guides/plug-and-play/Introduction.md)

## Examples [source code](https://github.com/spxbhuhb/zakadabar-stack/tree/master/lib/examples/src)

As a general convention, the documentation contains links to the working example codes.
Look for source code links, in most cases they are next to the title (see above, next to "Examples"). 
They take you to the actual example source code on GitHub.

Most of those links go the `lib:examples` or `lib:demo` subproject.

- [lib:examples](https://github.com/spxbhuhb/zakadabar-stack/tree/master/lib/examples) is a collection of working examples
- [lib:demo](https://github.com/spxbhuhb/zakadabar-stack/tree/master/lib/demo) is a working demo application