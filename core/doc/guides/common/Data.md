# Data

## Business Objects (BOs)

Business Objects store the data the application handles. They are the focal points, because they define
the data model. You can automatically generate frontend components, persistence APIs, routing from this
data model. BOs do not contain processing code, that is in the business logic components.

Here is a very simple BO from the examples. At first, it might seem a bit too much boilerplate to for that
one lonely `name`, but hold on, much is going on here.

```kotlin
@Serializable
class SimpleExampleBo(

    override var id: EntityId<SimpleExampleBo>,
    var name : String

) : EntityBo<SimpleExampleBo> {

    companion object : EntityBoCompanion<SimpleExampleBo>("zkl-simple-example")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name blank false min 1 max 30
    }

}
```

With the BO above, the stack:

- Handles all the communication between the frontend (browser, Android) and the backend.
- Provides you with:
    - Convenient, type safe data access functions out-of-the-box on the frontends: `SimpleExampleBo.all()`, `SimpleExampleBo.read(12)`, `bo.update()`
    - Validation:
        - Automatic in browser forms.
        - Automatic for incoming requests on the backend.  
        - On Android: by calling `bo.isValid` or `val report = bo.schema.validate()`
    - Generated code for (when using Bender):
        - browser: CRUD page, table and form
        - backend: business logic (for CRUD) and persistence api (with Exposed)
    - On browser frontends:
        - Automatic label and header translation in forms and tables.
        - Convenient builders for forms and tables. A fields with `+ bo::name` and have styles, validation, feedback
          out-of-the box.

These BO definitions are placed in `commonMain`. This means that the frontends and backends share the very same
code, therefore inconsistencies between the backend and the frontend are non-existing.

## BO Stereotypes

There are a few BO stereotypes:

| Type | Description |
| --- | --- |
| Basic | A data model without any specific use case. |
| Entity | An entity with a namespace and an id. May have complex structure, lists, nested classes, etc. Entities usually have a standard CRUD API (not mandatory). |
| Blob | A binary object, a file, image, video, etc. BLOBs belong to data records. |
| Query | A search operation requested by the frontend and executed by the backend. Queries do not modify data. |
| Action | A standalone operation such as login, logout. We also use actions when whenever we want to ensure ACID. |

## Namespace

All non-basic BO stereotypes have a `boNamespace`. This defines the endpoint the backend
offers and the frontends use. It is part of the URL, for example:

`/api/zkl-simple-example/entity/12`

For more information see [URLs](./URLs.md).

## Entity BO

### Write an Entity BO

You can write one manually by starting from one of the examples, or you can use the [Bender](../tools/Bender.md) tool
to generate one. Bender is a better choice when you need frontend and backend also, because it generates all the codes
you need for a quick start.

Once you have the BO, you probably want one or more of these (if you use Bender
most of these are generated for you):

* [Business Logic](../backend/BusinessLogic.md)
* [Persistence Api](../backend/PersistenceApi.md)
* [Form](../browser/builtin/Forms.md#Write-a-Form)
* [Table](../browser/builtin/Tables.md#Write-a-Table)
* [CRUD Target](../browser/builtin/Crud.md#Write-a-CRUD-Target)
    * [Route the CRUD Target](../browser/builtin/Crud.md#Route-a-CRUD-Target)
    * [Include the CRUD Target](../browser/builtin/Crud.md#Include-a-CRUD-Target)
* [Write an Inline CRUD](../browser/builtin/Crud.md#Write-an-Inline-CRUD)

### Use an Entity BO

On the frontend, shorthand functions make BO use really easy:

```kotlin
SimpleExampleBo.all() // lists all SimpleExamples
SimpleExampleBo.read(12) // read the record with id 12
SimpleExampleBo.read("aa-12") // read the record with id "aa-12", in case the record id is 

SimpleExampleBo
    .read(11)
    .apply { name = "this is the new name" }
    .update()

SimpleExampleBo.delete(11) // deletes record with id 11 on the server
bo.delete() // deletes the record on the server
```

## Query BO

### Write a Query BO

A query is quite similar to an entity BO but extends `QueryBo`, uses
`QueryBoCompanion` and have an `execute` method.

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="Namespace">

Namespace of query BOs depend on where the business logic is implemented.
You can add a query to an entity BL, in this case the namespace have to be the
same as in the entity. The example shows this approach.

If you implement a standalone query BL (see below), you have to choose a unique
namespace and pass it as the first parameter of QueryBoCompanion's constructor.

</div>

```kotlin
@Serializable
class SimpleExampleQuery(

    var name : String

) : QueryBo<SimpleQueryResult> {

    companion object : QueryBoCompanion<SimpleQueryResult>(SimpleExampleBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(SimpleQueryResult.serializer()))

    override fun schema() = BoSchema {
        + ::name blank false min 1 max 30
    }

}
```

On the backend you can extend an entity business logic module with a query
(see [Complex EBLB](../backend/BusinessLogic.md#Complex-EBLB)) or write a
[Standalone Query BL](../backend/BusinessLogic.md#Standalone-Query-BL)
independent of entities.

### Query Result

Queries always return with a list of result BOs. The type of result bo is
specified in the type parameter of `QueryBo` (and also in some other lines).

```kotlin
QueryBo<SimpleQueryResult>
```

The result BO can be any kind of BO.

### Use a Query

Call the `execute` method:

```kotlin
SimpleExampleQuery("name-to-look-for").execute().forEach {
    println(it)
}
```

## Action BO

### Write an Action

An action is quite similar to an entity BO but extends `ActionBo`, uses
`ActionBoCompanion` and have an `execute` method.

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="Namespace">

Namespace of action BOs depend on where the business logic is implemented.
You can add an action to an entity BL, in this case the namespace have to be the
same as in the entity. The example shows this approach.

If you implement a standalone action BL (see below), you have to choose a unique
namespace and pass it as the first parameter of ActionBoCompanion's constructor.

</div>

```kotlin
@Serializable
class SimpleExampleAction(

    var name : String

) : ActionBo<ActionStatusBo> {

    companion object : ActionBoCompanion<SimpleExampleAction>(SimpleExampleBo.boNamespace)

    override suspend fun execute() = comm.action(this, serializer(), ActionStatusBo.serializer())

    override fun schema() = BoSchema {
        + ::name blank false min 1 max 30
    }

}
```

On the backend you can extend an entity business logic module with an action
(see [Complex EBLB](../backend/BusinessLogic.md#Complex-EBLB)) or write a
[Standalone Action BL](../backend/BusinessLogic.md#Standalone-Action-BL)
independent of entities.

### Action Result

Actions always return with a single result BO. The type of the result bo is
specified in the type parameter of `ActionBo` (and also in some other lines).

```kotlin
ActionBo<ActionStatusBo>
```

The result BO can be any kind of BO, `ActionStatusBo` is a generic class provided
for actions which have a simple `success` flag and `reason` for explanation:

```kotlin
@Serializable
class ActionStatusBo(
    val success: Boolean = true,
    val reason: String? = null
) : BaseBo
```

### Use an Action

Call the `execute` method:

```kotlin
val result = SimpleExampleAction("parameter").execute()

if (!result.success) {
    println("failed to execute the action the reason is: ${reason ?: "unknown"}")
} else {
    println("successful execution")
}
```

## Blob BO

Blobs are handled by the [lib:blobs](../plug-and-play/blobs/Introduction.md) plug-and-play module.

## Data Validation

Data validation uses [BoSchema](/src/commonMain/kotlin/zakadabar/stack/data/schema/BoSchema.kt)
definitions.

BoSchema focuses more on programmer convenience than on performance. It is not really suitable for validating large
batches of data like thousands of transactions from XML files.

In practice, you use the validation when:

* the user fills a form, and you want to know if everything is OK
* the frontend sends some data to the backend, and you want to check that everything is OK

Additionally, you need the information about what's wrong when the user enters invalid data, this needs a properly
formatted, translated message.

Technically if you follow the guidelines you should not be able to send data from the client that fails to validate on
the server because you've already validated it on the client.

This of course does not free you from validation on the server side, it just means that you usually don't have to worry
much about it. (Considering that the standard backend modules automatically validate incoming request
with the schema, you really don't have to worry about it).

<div data-zk-enrich="Note" data-zk-flavour="Secondary" data-zk-title="Schema In the Future">

We are not perfectly happy with the way schemas are defined now. We have to write fields
twice, optional flag is in the constructor, constraints are in the schema, it is a bit
confused. 

This reason for this is that so far we couldn't find a way to bind things together better,
while sticking to basic Kotlin.

Delegates would be a possible solution but defining all properties with a delegate means that
all properties would have an object created for them and that would multiply the number of 
objects created. Doesn't sound that good when you work with a list of thousands of BOs.

Spring uses annotations, but those are cumbersome and hard to read. Same goes for the
defaults in the constructor, they make reading the code much harder.

The best way may be something CliKt uses: `var name by string() min 10 max 20` in the
class body. To make this viable we would need to turn this into a simple `String` field
and store the schema data separately.

We have to investigate this issue a bit more and decide how to go on. If you have an
idea, let us know.

</div>

### Write a Schema

To write a schema, override the `schema` function of the BO as the examples show above.

### Get a Default

In Kotlin, you have to initialize properties manually. This is a very good practice,
but it is a real hassle and many-many cases it is trivial what we want to start with.

You can set the defaults in the constructor, but it is not necessary. To get an instance
of your BO with the defaults specified in the schema use the `default` method.

```kotlin
val bo = default<SimpleExampleBo> {  }
```

`default` uses the schema to decide the defaults for all fields and sets them.
Use the builder function to apply manual modifications as needed:

```kotlin
val bo = default<SimpleExampleBo> { 
    name = "this is the name"
}
```

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Not for Many">

Use `default` only when you don't need large number of records. It is not suitable
for creating tens of thousands of instances. Declare your class as a data class
instead, create one instance with default and then copy it as many times as you need.

</div>


### Use a Schema

[ZkForm](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/form/ZkForm.kt) validates the BO automatically.

To validate manually:

```kotlin
val bo = default<SimpleExampleBo> { }
if (! bo.isValid) {
    println("default BO is actually invalid")
}
```

The schema actually builds a validation report. You can get this reportby
using `bo.schema().validate()` instead of `isValid`:

```kotlin
val bo = default<SimpleExampleBo> { }
bo.schema().validate().fails.forEach {
    println("${it.key} ${it.value}")
}
```

## Serialization

Serialization of BOs use kotlinx.serialization. Add the `@Serializable`
annotation in front of the BO class.