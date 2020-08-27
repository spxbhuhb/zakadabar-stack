# Data

## Query Data

Example: [Query Data Example](https://github.com/spxbhuhb/zakadabar-samples/tree/master/01-beginner/query-data)

### Defining a Query DTO

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

## Record Data

Example: [Zakadabar Module Template]()

### Defining a Record DTO

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

To create the backend routing for record DTO use the [recordRestApi](../../../src/jvmMain/kotlin/zakadabar/stack/backend/extend/restApi.kt)
function.

```kotlin
object Module : BackendModule() {

    override val uuid = Template.uuid

    override fun install(route: Route) {
        recordRestApi(route, TemplateRecordBackend, TemplateRecordDto::class, TemplateRecordDto.type)
    }
}
```

To add backend REST functions to a record DTO implement the RecordRestBackend interface.

```kotlin
object TemplateRecordBackend : RecordRestBackend<TemplateRecordDto> {

    override fun query(executor: Executor, id: Long?): List<TemplateRecordDto> = transaction {

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

## Entities


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

### Writing a Schema

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

### Using a Schema in a Form

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

### Validating data on the Backend

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