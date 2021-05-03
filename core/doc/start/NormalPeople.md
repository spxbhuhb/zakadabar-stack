## Introduction

The Zakadabar stack is about sensible defaults. We really try not to force you to use a specific component or walk a
specific way. You can replace the whole frontend, the whole backend, or both (which makes little sense, but actually it
does make sense).

However, the point of sensible defaults is that in most cases we don't want to replace them.

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
the [Application Template](https://github.com/spxbhuhb/zakadabar-application-template). Use GitHub's "Use this template"
function to create your own repository that is a copy of the application template and then follow the steps in the
README.

The template is a fully working application which you can customize by setting the project name and running one gradle
task (see README.md for details).

After customization, you can run another task and have a Zip file and/or Docker images ready for deployment.

Technically you are ready-to-go with the template. If you would like to learn about the main concepts of the stack, read
on.

## Data Transfer Objects

The stack is all about handling DTOs (data transfer objects). These define the API between the frontend and the backend.

Here is a very simple DTO from our marina demo. At first, it might seem a bit too much boilerplate to for that two
fields, but hold on, much is going on here.

```kotlin
@Serializable
data class SeaDto(

    override var id: RecordId<SeaDto>,
    var name: String

) : RecordDto<SeaDto> {

    companion object : RecordDtoCompanion<SeaDto>({ namespace = "sea" })

    override fun getRecordType() = namespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name min 1 max 30
    }

}
```

With the DTO above, the stack:

1. Handles all the communication between the frontend (browser, Android) and the backend.
1. Provides you with:
    1. Convenient, type safe data access functions out-of-the-box on the frontend: `SeaDto.all()`, `SeaDto.read(12)`
       , `dto.update()`
    1. Convenient, type safe base class on the backend.
    1. Automatic data validation in forms based on the schema.
    1. On browser frontends:
        1. Automatic form generation.
        1. Automatic label and header translation in forms and tables.
        1. Convenient builders for forms and tables. A fields with `+ dto::name` and have styles, validation, feedback
           out-of-the box.

Also, these DTO definitions are placed in `commonMain`. This means that the frontends and backends share the very same
code, therefore inconsistencies between the backend and the frontend are non-existing.

As we built on Kotlin, everything is types safe, no more why is this `undefined`.



