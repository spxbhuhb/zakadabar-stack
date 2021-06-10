# Forms

[ZkForm](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/form/ZkForm.kt) provides
the following functions out of the box:

- convenient builder functions
- component library for common data types and use cases
- automatic labeling based on property name
- automatic data validation and user feedback based on the schema
- automatic handling of CRUD submits
- mode-aware builders (for example: don't show ID for create)
- automatic translation of titles, explanations

<div data-zk-enrich="Note" data-zk-flavour="Secondary" data-zk-title="Status">

There are a few common data types that we haven't implemented yet.

Most notably date and time fields are not supported as of now.

These are on the way, local dates are scheduled for June, others we'll add
when needed.

</div>

## Write a Form [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/form/FormSimpleExample.kt)

To start quickly, you can use the  [Bender](../../tools/Bender.md) to generate a form. 
Alternatively, you can build your form manually:

1. create a [BO](../../common/Data.md) to store the data of the form
1. create a class that extends [ZkForm](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/form/ZkForm.kt)
1. override `onCreate` to build the form

```kotlin
class SimpleExampleForm : ZkForm<SimpleExampleBo>() {
    override fun onCreate() {
        super.onCreate()

        build(translate<SimpleExampleForm>()) {

            + section(strings.basics) {
                + bo::id
                + bo::name
            }

        }
    }
}
```

This simple form above looks like this:

<div data-zk-enrich="FormSimpleExample"></div>

`ZkForm` is a [ZkElement](../structure/Elements.md), you can use the standard element functions.

### Build Function

```kotlin
build(translate<SimpleExampleForm>())
```

`build` is a helper function to add default surrounding to the form:

* set the page title
* create surrounding div to put the form into
* add mode aware buttons
* add list of invalid fields

Build is not strictly necessary, it is a simple convenience function. There are 
many forms that does not call build.

### Sections

Sections are building blocks for forms. When you have a large complicated form,
you might want to have more than one section. This helps your user by visually
grouping form fields into separate parts.

```kotlin
+ section("Section Title", "Section summary.") {
    + bo::optStringValue
}
```

Sections are optional.

Customize sections with the function parameters:

| Parameter | Description |
| --- | --- |
| `title` | Title of the section. |
| `summary` | A short text that describes what this section contains, helps the user to understand the context. |
| `fieldGrid` | When true (default) the section contains a grid, and the builder function is executed inside the grid. When false there is no grid. |
| `css` | CSS class to add. |

Here is an example with multiple sections. [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/form/FormMultiSectionExample.kt)

The first section is usual, but the second switches off the default field grid, 
therefore the labels and the inputs are not in a grid, but below each other.

<div data-zk-enrich="FormMultiSectionExample"></div>

## Fields

Fields of a form are usually bound to a property of the BO the form handles. When
the user changes the value of an input, the value in the BO also changes.

This does not work the other way, if you change the value in the BO, the form field
does not update automatically. Please do not build on this behaviour because
we might change this in the future.

### Built-In Fields [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/form/FormBuiltinExample.kt)

This form shows all built in field types.

<div data-zk-enrich="FormBuiltinExample"></div>

### Find Fields

Use the `find` extension function to find the field element that belongs to a 
given property easily:

```kotlin
+ bo::name.find()
```

### Add Fields

You can add fields three different ways:

- use the property shorthand
- use a helper function
- create the instance manually

### Property Shorthands

This is the easiest way to add a field. You can use this for fields when building 
the field does not require additional information, like list of options.

```kotlin
+ bo::stringValue
```

Types that support the property shorthand:

- own entity id
- Boolean
- Enum, Enum?
- Int, Int?
- Long, Long?
- Double, Double?
- Instant, Instant?
- Secret, Secret?
- String, String?
- UUID, UUID?

### Helper functions

#### constString

To add a constant string as a field, use the `constString` function:

```kotlin
constString("label") { "value" }
```

#### opt(Boolean)

To add an optional boolean property use the `opt` function. This function
lets the user select from three values:

- not selected
- true
- false

You can customize the true and false texts with the function parameters.

```kotlin
+ opt(bo::optBooleanValue, stringStore.trueText, stringStore.falseText)
```

#### select(EntityId)

To add a select for `EntityId` and `EntityId?` fields, use the `select` helper function.
Fetching all references works for small data sets, but consider more advanced
approaches (cached entity comm, global pre-fetch, filtered select) when the
number of options is high.

```kotlin
+ select(bo::recordSelectValue) { 
    ExampleReferenceBo.all().by { it.name }
}
```

#### select(String)

To add a select for `String` and `String?` fields, use the `select` helper function.

```kotlin
+ select(
    bo::stringSelectValue, 
    options = listOf("option 1", "option 2", "option3")
)
```

#### textarea

To use a textarea for a string field, use the `textarea` function:

```kotlin
+ textarea(bo::textAreaValue)
```

To set the inner area height:

```kotlin
+ textarea(bo::value) { area.style.height = 60.vh }
```

### Customization

#### label

Use the `label` function to specify a customized label:

```kotlin
+ textarea(bo::textAreaValue) label "My Label"
```

#### readOnly

Use the `readOnly` function to set a field read-only:

```kotlin
+ bo::name readOnly true
```

#### newSecret

Use the `newSecret` function to set autocomplete of secret fields to `new-password`.

```kotlin
+ bo::password newSecret true
```

### Manual Create

You can create fields instances manually and add them to the form. Depending on
the context, you might have to qualify `this` as the example does.

```kotlin
+ ZkStringField(this@BuiltinForm, bo::stringValue).also {
    this@BuiltinForm.fields += it 
}
```

### Write a Field

You can easily write your own form field types by extending
[ZkFieldBase](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/form/fields/ZkFieldBase.kt).

Check the built-in fields for examples.

## Use a Form

### Crud

Set `editorClass` of the crud object. For more information see [Crud](./Crud.md).

```kotlin
object BuiltinCrud : ZkCrudTarget<BuiltinDto>() {
    init {
        companion = BuiltinDto.Companion
        dtoClass = BuiltinDto::class
        editorClass = BuiltinForm::class
        tableClass = BuiltinTable::class
    }
}
```

### Inline, Fetched Data

```kotlin
@PublicApi // example code
class FormFieldsFetched(
    private val entityId: EntityId<BuiltinBo>
) : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        io {
            val form = BuiltinForm()
            form.bo = BuiltinBo.read(entityId)
            form.mode = ZkElementMode.Update

            + div(zkPageStyles.content) {
                + form
            }
        }
    }

}
```

### Inline, Default Data

To use a form with default BO values set `bo` with the `default` function. For more information see [Get a Default](../../common/Data.md#Get-a-Default).

```kotlin
class FormFieldsDefault : ZkElement() {

    override fun onCreate() {
        super.onCreate()

        val form = BuiltinForm()
        form.bo = default()
        form.mode = ZkElementMode.Update

        + div(zkPageStyles.content) {
            + form
        }
    }

}
```

## Images

TBW
