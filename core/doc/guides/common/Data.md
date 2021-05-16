# Data

## Data Transfer Objects (DTOs)

The stack is all about handling DTOs (data transfer objects). These define the API between the frontends and the backend.

Here is a very simple DTO from the examples. At first, it might seem a bit too much boilerplate to for that two
fields, but hold on, much is going on here.

```kotlin
@Serializable
data class SimpleExampleDto(

    override var id: RecordId<SimpleExampleDto>,
    var name: String

) : RecordDto<SimpleExampleDto> {

    companion object : RecordDtoCompanion<SimpleExampleDto>("simple-example")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name min 1 max 30 blank false default "Example Name"
    }

}
```

With the DTO above, the stack:

1. Handles all the communication between the frontend (browser, Android) and the backend.
1. Provides you with:
    1. Convenient, type safe data access functions out-of-the-box on the frontends: `SimpleExampleDto.all()`, `SimpleExampleDto.read(12)`, `dto.update()`
    1. Convenient, type safe base module on the backend.
    1. Automatic data validation in forms based on the schema.
    1. On browser frontends:
        1. Automatic form generation.
        1. Automatic label and header translation in forms and tables.
        1. Convenient builders for forms and tables. A fields with `+ dto::name` and have styles, validation, feedback
           out-of-the box.

Also, these DTO definitions are placed in `commonMain`. This means that the frontends and backends share the very same
code, therefore inconsistencies between the backend and the frontend are non-existing.

## DTO Types

There are a few DTO stereotypes:

| Type | Description |
| --- | --- |
| Basic | A data model without any specific use case. |
| Record | A data record with a namespace and an id. May have complex structure, lists, nested classes, etc. Records usually have a standard CRUD API (not mandatory). |
| Blob | A binary object, a file, image, video, etc. BLOBs belong to data records. |
| Query | A search operation requested by the frontend and executed by the backend. Queries do not modify data. |
| Action | A standalone operation such as login, logout. We also use actions when whenever we want to ensure ACID. |

## Important points

* all DTOs should be in `commonMain`, this ensures that frontend and backend uses the same API
* do not copy and paste directories in IDEA, the package names will be wrong and all hell will get lose, create the
  directory and then copy files

## Write a Record DTO

Copy one and change the fields, the namespace, and the schema. Here is an example to copy.

Namespace is part of the URL the frontend uses to communicate with the backend. The only important
thing is that it should be unique as it identifies the backend that will handle the request. It
will be part of the API URL, check [URLs](./URLs.md) for more information.

```kotlin
@Serializable
data class SimpleExampleDto(

    override var id: RecordId<SimpleExampleDto>,
    var name: String

) : RecordDto<SimpleExampleDto> {

    companion object : RecordDtoCompanion<SimpleExampleDto>(dtoNamespace = "simple-example")

    override fun getDtoNamespace() = dtoNamespace
    override fun comm() = comm

    override fun schema() = DtoSchema {
        + ::id
        + ::name min 1 max 30 blank false default "Example Name"
    }

}
```

Now, that you have a record DTO, you can:

* [Write a Record Backend](../backend/RecordBackends.md#Write-a-Record-Backend)
* [Use a Record DTO](./Data.md#Use-a-Record-DTO)
* [Write a Form](../browser/builtin/Forms.md#Write-a-Form)
* [Write a Table](../browser/builtin/Tables.md#Write-a-Table)
* [Write a CRUD](../browser/builtin/Crud.md#Write-a-CRUD)
* [Route the CRUD](../browser/builtin/Crud.md#Route-a-CRUD)
* [Include the CRUD](../browser/builtin/Crud.md#Include-a-CRUD)

## Use a Record DTO

On the frontend, shorthand functions make DTO use really easy:

```kotlin
SimpleExampleDto.all() // lists all SimpleExamples
SimpleExampleDto.read(12) // read the record with id 12
SimpleExampleDto.read("aa-12") // read the record with id "aa-12"

val dto = SimpleExampleDto.read(11)
dto.name = "this is the new name"
dto.update() // updates the record on the server

dto.delete() // deletes the record on the server
```

## Write a Query

TODO

## Use a Query

Call the `execute` method:

```kotlin
SearchShipsQuery().execute().forEach {
    println(it)
}
```

## Write an Action

TODO

## Use an Action

Call the `execute` method:

```kotlin
val result = ShootAtShipAction().execute()

// We have a "success" property here because result type of ShootAtShipAction is an ActionStatusDto.
// If you use another result type, you may not have the result field here.

if (result.success) {
    TODO()
}
```

## Data Validation

Data validation uses [DtoSchema](/src/commonMain/kotlin/zakadabar/stack/data/schema/DtoSchema.kt)
definitions.

DtoSchema focuses more on programmer convenience than on performance. It is not really suitable for validating large
batches of data like thousands of transactions from XML files.

In practice, you use the validation when:

* the user fills a form, and you want to know if everything is OK
* the frontend sends some data to the backend, and you want to check that everything is OK

Additionally, you need the information about what's wrong when the user enters invalid data, this needs a properly
formatted, translated message.

Technically if you follow the guidelines you should not be able to send data from the client that fails to validate on
the server because you've already validated it on the client.

This of course does not free you from validation on the server side, it just means that you usually don't have to worry
much about it as a developer.

## Write a Schema

TODO

## Use a Schema

[ZkForm](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/form/ZkForm.kt) validates the dto automatically.

To validate manually:

```kotlin
val dto: ShipDto = default { }
if (! dto.isValid) {
    println("default ship DTO is actually invalid")
}
```

To inspect fails:

```kotlin
val dto: ShipDto = default { }
dto.schema().validate().fails.forEach {
    println("${it.key} ${it.value}")
}
```

## Serialization

Serialization of DTOs use kotlinx.serialization. Just add the `@Serializable`
annotation in front of the DTO class.

When serializing kotlinx.datetime data types the appropriate annotation is needed:

```kotlin
@file:UseSerializers(
  InstantAsStringSerializer::class,
  OptInstantAsStringSerializer::class
)
```
