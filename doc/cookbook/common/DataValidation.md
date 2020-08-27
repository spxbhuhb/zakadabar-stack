# Data Validation

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

## Writing a Schema

```kotlin
// This is the DTO for a rabbit. It has a schema that checks the name.

class RabbitDto(
    val id : Long,
    var name : String
) : DtoWithIdContract<RabbitDto> {

    companion object : DtoWithIdCompanion<RabbitDto>()
    
    val schema = DtoSchema.build {
        + ::name max 20 min 2 notEquals "Caerbannog"
    }
    
    override fun comm() = comm
}
```

## Using a Schema in a Form

This is a frontend element that is basically form to edit the name of the rabbit.
The form won't let the user click on the submit button before the validation by
the schema returns with true.

Also, standard form fields like ValidatedStringInput realize that there is a schema
underneath and validate the data the user types in real-time, show errors to the user. 

```kotlin
class RabbitEditor(dto : RabbitDto) : ComplexElement() {
    override fun init() : RabbitEditor {
        build {
            + form(dto) {
                + RabbitDto::name
                + Submit
            }
        }
    }
}
```

## Validating data on the Backend

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
