# Data

Examples:

* [Query Data Example - TODO](https://github.com/spxbhuhb/zakadabar-samples/tree/master/01-beginner/query-data)
* [Record Data Example - TODO](https://github.com/spxbhuhb/zakadabar-samples/tree/master/01-beginner/record-data)
* [Entity Data Example - TODO](https://github.com/spxbhuhb/zakadabar-samples/tree/master/01-beginner/entity-data)

## Terminology

| Generic Concept | Explanation |
| ---- | ---- |
| Table | An SQL table for persistence, the stack uses Exposed tables. |
| DAO | A Data Access Object used on the backend that makes handling the data easier, the stack uses Exposed DAOs. |
| DTO | A Data Transfer Object that we use to transfer data between the client and the frontend. |

## DTO Types

There are three DTO types in the stack:

| Type | Use |
| ---- | --- |
| Query DTO | The backend sends the result of a complex query (joins etc.) to the frontend. |
| Record DTO | Plain SQL tables with a Long id. Usually with standard REST API. |
| Entity DTO | Entities in the entity tree (see below). These are meant to build a hierarchical structure, like folders and files. Standard REST API with hierarchy support. |

### Query DTOs

Used mostly for lists which contain just some parts of the actual data. Like tables which contain only the main
field of the backing records.

Query DTOs are mostly read only, creation and update usually uses record and/or entity DTOs.

### Record DTOs

These are good old SQL records of SQL tables.

If your DTO implement the [DtoWithRecordContract](../../../src/commonMain/kotlin/zakadabar/stack/extend/DtoWithRecordContract.kt)
interface, you can use a [RecordRestComm](../../../src/jsMain/kotlin/zakadabar/stack/frontend/comm/rest/RecordRestComm.kt)
to handle your data easily.

### Entity DTOs

The stack maintains a so-called **Entity Tree**. The nodes and leaves are entities
stored in the SQL table `t_3a8627_entities`. 

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
it is better to store order records outside of the tree as there are many, and 
you will probably write a specific query API for it anyway.

That said, entities are very useful because they give you a well-defined and easy
way to extend the system. After you define a new entity type most parts of the 
application will automatically include it.

## Query DTOs

### Write a Query DTO

As query DTOs are only used for getting data from the server we don't define a schema and a type.

```kotlin
@Serializable
data class QueryDto(
    val field1 : String,
    val field2 : String,
)
```

### Backend for a Query DTO

```kotlin

```

### Frontend for a Query DTO

```kotlin

```

## Record DTOs

### Write a Record DTO

```kotlin
@Serializable
data class TemplateRecordDto(

    override val id: Long,

    val templateField1 : String,
    val templateField2 : String,

) : DtoWithRecordContract<TemplateRecordDto> {

    companion object : DtoWithRecordCompanion<TemplateRecordDto>() {
        val type = "${Template.shid}/template-record"
    }

    override fun schema() = DtoSchema.build {
        + ::templateField1 min 1 max 100
        + ::templateField2 min 2 max 50
    }

    override fun comm() = comm

}
```

### Backend for a Record DTO

To create a backend SQL table for a Record DTO:

```kotlin
object TemplateRecordTable : LongIdTable("t_${Template.shid}_records") {

    val templateField1 = varchar("template_field_1", length = 100)
    val templateField2 = varchar("template_field_2", length = 50)

}
```

To create a backend DAO for a Record DTO:

```kotlin
class TemplateRecordDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TemplateRecordDao>(TemplateRecordTable)

    var templateField1 by TemplateRecordTable.templateField1
    var templateField2 by TemplateRecordTable.templateField2

    fun toDto() = TemplateRecordDto(
        id = id.value,
        templateField1 = templateField1,
        templateField2 = templateField2
    )

}
```

To create the backend routing for record DTO use the [recordRestApi](../../../src/jvmMain/kotlin/zakadabar/stack/backend/extend/restApi.kt)
function.

```kotlin
object Module : BackendModule() {

    override val uuid = Template.uuid

    override fun install(route: Route) {
        restApi(route, TemplateRecordBackend, TemplateRecordDto::class, TemplateRecordDto.type)
    }
}
```

To add backend REST functions to a record DTO implement the RecordRestBackend interface.

```kotlin
object TemplateRecordBackend : RecordRestBackend<TemplateRecordDto> {

    override fun query(executor: Executor, id: Long?, parentId : Long?, parameters : Parameters): List<TemplateRecordDto> = transaction {

        if (id == null) {
            TemplateRecordDao.find { TemplateRecordTable.id eq id }
        } else {
            TemplateRecordDao.all()
        }
            .filter { false /* TODO Authorization */ }
            .map { it.toDto() }

    }

    override fun create(executor: Executor, dto: TemplateRecordDto) = transaction {

        // TODO authorization

        val dao = TemplateRecordDao.new {
            templateField1 = dto.templateField1
            templateField2 = dto.templateField2
        }

        dao.toDto()
    }

    override fun update(executor: Executor, dto: TemplateRecordDto) = transaction {

        // TODO authorization

        val dao = TemplateRecordDao[dto.id]

        dao.templateField1 = dto.templateField1
        dao.templateField2 = dto.templateField2

        dao.toDto()
    }

}
```

### Frontend for a Record DTO

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

    override fun getType() = type

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