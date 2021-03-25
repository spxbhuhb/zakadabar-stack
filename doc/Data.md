# Data

## General Concepts

| Name | Explanation |
| ---- | ---- |
| Table | An SQL table for persistence, the stack uses Exposed tables, but you can use whatever persistence you want actually. |
| DAO | A Data Access Object used on the backend that makes handling the data easier, the stack uses Exposed DAOs. |
| DTO | A Data Transfer Object that we use to transfer data between the frontend and the backend. |
| Data Record | A set of data fields which belong together logically. We use "Data Record" when we talk about abstract data model records. One data record may have one or more SQL table rows. |
| Query | Everything that belongs to one data query: Query Parameter DTO, Query Response DTO, backend implementation. |
| Query Parameter DTO | The frontend sends query parameters to the backend. Not for CRUD. |
| Query Response DTO |  The backend sends the result of a query to the frontend. Not for CRUD. |
| Record DTO | DTO for a data record which has a type and an id. Not the very same as an SQL table record, but close. Usually with standard CRUD API. |

## Data Records

Data records have a:
- Record DTO
- [DTO Frontend](../frontend/DtoFrontend.md)
- [DTO Backend](../backend/DtoBackend.md)

### Write a Record DTO

Working example:
  - [The Old Man from Scene 24 - RabbitDto.kt](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/the-old-man-from-Scene-24/src/commonMain/kotlin/zakadabar/samples/holygrail/data/rabbit/RabbitDto.kt)
  - [The Old Man from Scene 24 - RabbitBackend.kt](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/the-old-man-from-Scene-24/src/jvmMain/kotlin/zakadabar/samples/holygrail/backend/rabbit/RabbitBackend.kt)

To write a record DTO:

- write the DTO class, RabbitDto in this case
- write the DTO frontend
- write the DTO backend

```kotlin
@Serializable
data class RabbitDto(

    override val id: Long,
    val name: String,
    val color: String

) : RecordDto<RabbitDto> {

    companion object : RecordDtoCompanion<RabbitDto>() {

        override val type = "${Rabbit.shid}/rabbit"

        override val queries = Queries.build {
            + RabbitSearch.Companion
            + RabbitColors.Companion
        }

    }

    override fun schema() = DtoSchema.build {
        + ::name max 20 min 2 notEquals "Caerbannog"
    }

    override fun getType() = recordType

    override fun comm() = RabbitDto.comm
}
```

### Backend for a Record DTO

To create a backend SQL table for a data record:

```kotlin
object RabbitTable : LongIdTable("t_${Stack.shid}_rabbits") {

    val name = varchar("name", 20)
    val color = varchar("color", 20)

    fun toDto(row: ResultRow) = RabbitDto(
        id = row[id].value,
        name = row[name],
        color = row[color]
    )

}
```

To create a backend DAO for a data record:

```kotlin
class RabbitDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<RabbitDao>(RabbitTable)

    var name by RabbitTable.name
    var color by RabbitTable.color

    fun toDto() = RabbitDto(
        id = id.value,
        name = name,
        color = color
    )
}
```

To add a backend for a record DTO extend [DtoBackend](../../../src/jvmMain/kotlin/zakadabar/stack/backend/data/DtoBackend.kt).

- `init` is called during server startup
- `install` is called during server startup to add Ktor routing, see [URLs](../common/URLs.md)
- `query`, `all`, `create`, `read`, `update`, `delete` are automatically handled when `install` adds the routes

```kotlin
object RabbitBackend : DtoBackend<RabbitDto>() {

    override val dtoClass = RabbitDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                RabbitTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
        route.query(RabbitSearch::class, RabbitBackend::query)
        route.query(RabbitColors::class, RabbitBackend::query)
    }

    private fun query(executor: Executor, query: RabbitSearch) = transaction {
        RabbitTable
            .select { RabbitTable.name like query.name }
            .map(RabbitTable::toDto)
    }

    private fun query(executor: Executor, query: RabbitColors) = transaction {
        RabbitTable
            .slice(RabbitTable.color)
            .select { RabbitTable.name inList query.rabbitNames }
            .distinct()
            .map { Color(it[RabbitTable.color]) }
    }

    override fun all(executor: Executor) = transaction {
        RabbitTable
            .selectAll()
            .map(RabbitTable::toDto)
    }

    override fun create(executor: Executor, dto: RabbitDto) = transaction {
        RabbitDao.new {
            name = dto.name
            color = dto.color
        }.toDto()
    }

    override fun read(executor: Executor, id: Long) = transaction {
        RabbitDao[id].toDto()
    }

    override fun update(executor: Executor, dto: RabbitDto) = transaction {
        val dao = RabbitDao[dto.id]
        dao.name = dto.name
        dao.color = dto.color
        dao.toDto()
    }

    override fun delete(executor: Executor, id: Long) {
        RabbitDao[id].delete()
    }
}
```

### Frontend for a Record DTO

To have a frontend for a data record extend [DtoFrontend](../../../src/jsMain/kotlin/zakadabar/stack/frontend/data/DtoFrontend.kt).

### Use Comm without DtoFrontend

To add a comm to a record DTO use RecordRestComm. The URL is important, it has to match with the URL on the backend.
Best is to define a `type` field in the DTO's companion.

```kotlin
@PublicApi
object Module : FrontendModule() {
 
    override val uuid = Template.uuid
 
    override fun init() {
         TemplateRecordDto.comm = RecordRestComm(TemplateRecordDto.type, TemplateRecordDto.serializer()) 
    }

}
```

## Queries

### Write a Query

Working example:
  - [The Old Man from Scene 24 - RabbitDto.kt](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/the-old-man-from-Scene-24/src/commonMain/kotlin/zakadabar/samples/holygrail/data/rabbit/RabbitDto.kt)
  - [The Old Man from Scene 24 - RabbitBackend.kt](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/the-old-man-from-Scene-24/src/jvmMain/kotlin/zakadabar/samples/holygrail/backend/rabbit/RabbitBackend.kt)

- write the parameter class, RabbitColors in this case
- write the response class, Color in this case
- add the query to the base DTO class
- add the query to the DTO backend

Write the parameter class:

```kotlin
@Serializable
data class RabbitColors(

    val rabbitNames: List<String> = emptyList()

) : QueryDto {

    suspend fun execute() = comm.query(this, serializer(), ListSerializer(Color.serializer()))

    companion object : QueryDtoCompanion(RabbitDto.Companion)

}
```

Write the response class:

```kotlin
@Serializable
data class Color(
    val color: String
)
```

Add the query to the DTO class:

```kotlin
@Serializable
data class RabbitDto : RecordDto<RabbitDto> {

    companion object : RecordDtoCompanion<RabbitDto>() {

        override val queries = Queries.build {
            + RabbitColors.Companion  // <- this is what you add
        }

    }
}
```

Add the query to the DTO backend:

```kotlin
object RabbitBackend : DtoBackend<RabbitDto>() {

    override fun install(route: Route) {
        route.query(RabbitColors::class, RabbitBackend::query)  // <- this is what you add
    }

    private fun query(executor: Executor, query: RabbitColors) = transaction {   // <- this is what you add
        RabbitTable
            .slice(RabbitTable.color)
            .select { RabbitTable.name inList query.rabbitNames }
            .distinct()
            .map { Color(it[RabbitTable.color]) }
    }
}
```

### Use a Query

Call the `execute` method:

```kotlin
RabbitColors().execute().forEach { println(it) }
```

### Shorthand for Query

Use shorthand when the query returns with the base DTO, RabbitDto in this case

```kotlin
@Serializable
data class RabbitSearch(
    val name: String
) : QueryDto {
    suspend fun execute() = comm.query(this, serializer())
    companion object : QueryDtoCompanion(RabbitDto.Companion)
}
```

## Entity DTOs

### Write an Entity DTO

```kotlin
@Serializable
data class TemplateEntityDto(

    override val id: Long,
    override val entityRecord: EntityRecordDto?,

    val name: String,
    val templateField1: String,
    val templateField2: String,

    ) : DtoWithEntityContract<TemplateEntityDto> {

    companion object : DtoWithEntityCompanion<TemplateEntityDto>() {
        override val type = "${Template.shid}/template-entity"
    }

    override fun schema() = DtoSchema.build {
        + ::name min 1 max 100 // this comes from stack entity name limit
        + ::templateField1 min 1 max 100
        + ::templateField2 min 2 max 50
    }

    override fun getType() = recordType

    override fun comm() = comm

}
```

### Get Entity Related URLs

To get URL for the Entity Record DTO itself by:

```kotlin
val url = EntityRecordDto.dtoUrl(id)
    // OR when dto is an EntityRecordDto
val url = dto.dtoUrl()
```

To get URL for the Entity Record DTO and a view:

```kotlin
val url = EntityRecordDto.viewUrl(id, "read")
    // OR when dto is an EntityRecordDto
val url = dto.viewUrl("read")
```

To get URL for the last revision of the binary content of the entity:

```kotlin
val url = EntityRecordDto.revisionUrl(id)
    // OR when dto is an EntityRecordDto
val url = dto.revisionUrl()
```

To get URL for a given revision of the binary content of the entity:

```kotlin
val revisionNumber = 23
val url = EntityRecordDto.revisionUrl(id, revisionNumber)
    // OR when dto is an EntityRecordDto
val url = dto.revisionUrl(revisionNumber)
```

## Data Validation

Data validation uses DtoSchema definitions. Theoretically you want to validate data when
you transfer it from one place to another, this is why it is linked with the DTO.

Validation in the Stack meant for validating user input. It focuses more on programmer convenience
than on performance. It is not really suitable for validating large batches of data like thousands
of transactions from XML files.

In practice, you use the validation when:

* the user fills a form, and you want to know if everything is OK
* the frontend sends some data to the backend, and you want to check that everything is OK

Additionally, you need the information about what's wrong when the user enters 
invalid data, this needs a properly formatted, nice, translated message.

Technically if you follow the guidelines you should not be able to send data from the
client that fails to validate on the server because you've already validated it on the client.

This of course does not free you from validation on the server side, it just means that
you usually don't have to worry much about it as a developer.

### Write a Schema

```kotlin
// This is the DTO for a rabbit. It has a schema that checks the name.

class RabbitDto(
    val name : String
) {
    companion object {
        val schema = DtoSchema.build {
            + ::name max 20 min 2 notEquals "Caerbannog"
        }
    }
}
```

### Use a Schema in a Form

This is a frontend element that is basically form to edit the name of the rabbit.
The form won't let the user click on the submit button before the validation by
the schema returns with true.

Also, standard form fields like ValidatedStringInput realize that there is a schema
underneath and validate the data the user types in real-time, show errors to the user. 

```kotlin
class RabbitEditor : ComplexElement() {
    override fun init() : RabbitEditor {
        build {
            + form(RabbitDto.Companion) {
                + RabbitDto::name
                + Submit
            }
        }
    }
}
```

### Validate Data on the Backend

DTOs have a few methods by default to check validation and get the validation result:

* isValid
* checkValidity
* requireValidity
* getValidityReport

```kotlin
// These are backend functions to check the validity of a rabbit.

fun checkValidRabbit(dto : RabbitDto) {
    dto.checkValidity() // throws IllegalStateException when invalid
}

fun requireValidRabbit(dto : RabbitDto) {
    dto.requireValidity() // throws IllegalArgumentException when invalid
}
```



### Entities

The stack maintains a so-called **Entity Tree**. The nodes and leaves are entities
stored in the SQL table `t_3a8627_entities`.

For each entity there is a row in the `t_3a8627_entities` SQL table and usually there
is be a record in some other table, depending on the `type` of the entity.

Points of interest:

* [EntityRecordDto](../../../src/commonMain/kotlin/zakadabar/stack/data/entity/EntityRecordDto.kt)
* [EntityDao](../../../src/jvmMain/kotlin/zakadabar/stack/backend/builtin/entities/data/EntityDao.kt)
* [EntityTable](../../../src/jvmMain/kotlin/zakadabar/stack/backend/builtin/entities/data/EntityDao.kt)

Many stack functions revolve around these entities as the stack stores most of 
its data as entities for example:

* user accounts
* access control objects (ACLs, roles, grants)
* folders

It is important, that the entity tree is not suitable for everything. For example:
it is better to store order records outside of the tree when there are many. 
You will probably write a specific query API for it anyway.

That said, entities are very useful because they give you a well-defined and easy
way to extend the system. After you define a new entity type most parts of the 
application will automatically include it.

## Serialization

Serialization of DTOs use kotlinx.serialization. Just add the `@Serializable`
annotation in front of the DTO class.

When serializing kotlinx.datetime data types the appropriate annotation is needed:

```kotlin
@file:UseSerializers(InstantAsStringSerializer::class)
```
