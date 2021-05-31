# Forms

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Note">

This documentation page needs rewrite: 
* it does not provide enough information,
* the BuiltinBo example is too complex to start with.
</div>

* Features:
    * component library for common data types and use cases
    * automatic labeling based on property name
    * automatic data validation and user feedback based on the schema
    * automatic handling of CRUD submits
    * mode-aware builders (for example: don't show ID for create)
    * automatic translation of titles, explanations
* Synthetic forms: build forms from the schema automatically

## Write a Form

1. create a [BO](../../common/Data.md) to store the data of the form
1. create a class that extends [ZkForm](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/form/ZkForm.kt)
1. override `onCreate` to build the form

```kotlin
class BuiltinForm : ZkForm<BuiltinDto>() {

    override fun onCreate() {
        super.onCreate()

        build(Strings.formFields) {

            + ZkFormStyles.twoPanels

            + section(Strings.mandatoryFields) {
                with(dto) {
                    + ::id
                    + ::booleanValue
                    + ::doubleValue
                    + ::enumSelectValue
                    + ::intValue
                    + ::instantValue
                    + ::secretValue
                    + select(::recordSelectValue) { ExampleReferenceDto.all().by { it.name } }
                    + ::stringValue
                    + select(::stringSelectValue, options = listOf("option1", "option2", "option3"))
                    + textarea(::textAreaValue)
                    + ::uuidValue
                }
            }

            + section(Strings.optionalFields) {
                with(dto) {
                    + opt(::optBooleanValue, "true", "false")
                    + ::optDoubleValue
                    + ::optEnumSelectValue
                    + ::optInstantValue
                    + ::optIntValue
                    + ::optSecretValue
                    + select(::optRecordSelectValue) { ExampleReferenceDto.all().by { it.name } }
                    + ::optStringValue
                    + select(::optStringSelectValue, options = listOf("option1", "option2", "option3"))
                    + textarea(::optTextAreaValue)
                    + ::optUuidValue
                }
            }

            + ZkButton(Strings.validate) {
                validate(true)
            }

        }
    }
}
```

## Use a Form - CRUD

Set `pageClass` of the crud object.

```kotlin
object BuiltinCrud : ZkCrudTarget<BuiltinDto>() {
    init {
        companion = BuiltinDto.Companion
        dtoClass = BuiltinDto::class
        pageClass = BuiltinForm::class
        tableClass = BuiltinTable::class
    }
}
```

## Use a Form - Direct, Fetched Data

```kotlin
object FormFieldsFetched : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        io {
            // Get your id in a more logical way, fetching all records just to get
            // an id is plain wrong. I just needed one make this example working.
            val id = BuiltinDto.all().first().id

            val form = BuiltinForm()
            form.dto = BuiltinDto.read(id)
            form.mode = ZkElementMode.Update

            + div(ZkPageStyles.content) {
                + form
            }
        }
    }

}
```

## Use a Form - Direct, Generated Data

```kotlin
object FormFieldsGenerated : ZkPage() {

    override fun onCreate() {
        super.onCreate()

        val form = BuiltinForm()
        form.dto = default()
        form.mode = ZkElementMode.Action

        + div(ZkPageStyles.content) {
            + form
        }
    }

}
```

## Images

To handle images:

* [Write a Blob Backend](../../common/Blobs.md#Write-a-Blob-Backend).
* Use [ZkImagesField](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/form/fields/ZkImagesField.kt).


