# Forms

Base class for forms is [ZkForm](/core/core/src/jsMain/kotlin/zakadabar/core/browser/form/ZkForm.kt). This is
a rather complex implementation of web browser forms with many-many functions to make form building as 
much comfortable as possible.

Features:

- convenient builder functions
- component library for common data types and use cases
- automatic labeling based on property name
- automatic data validation and user feedback based on the schema
- automatic handling of CRUD submits
- mode-aware builders (for example: don't show ID for create)
- automatic translation of titles, explanations

The form builder uses inline functions heavily. The reason for this is that readability of the code
is always a big problem for complex forms, so we've tried to make it as readable as possible.

For example, a complex field definition may look like:

```kotlin
+ bo::recordSelectValue query ::queryRecords onChange3 ::onRecordSelectChange saveAs ::recordSelectField
```

This also can be also written below. However, the syntax coloring (in IDEA) makes the one-liner above very
much easier to read.

```kotlin
bo::recordSelectValue
    .unaryPlus()
    .query(::queryRecords)
    .onChange3(::onRecordSelectChange)
    .saveAs(::recordSelectField)
```

## Write a Form [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/form/FormSimpleExample.kt)

Forms are linked to a business object, the type parameter of `ZkForm` is this business
object. The example below shows a very simple form, note that `build` and `section`
is optional, it sets the title, adds surrounding borders etc.

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

The form above looks like this:

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

Here is an example with multiple sections. [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/form/FormMultiSectionExample.kt)

The first section is usual, but the second switches off the default field grid, 
therefore the labels and the inputs are not in a grid, but below each other.

<div data-zk-enrich="FormMultiSectionExample"></div>

## Fields

You can add different kind of fields to forms:

- fields for properties of object instances
- fields for individual values
- special fields like filters

### Property Based Fields

These fields are linked to a property, typically one in the BO the form handles,
but that is actually not necessary. To add such a field to a form, simply use:

```kotlin
+ bo::stringValue
```

The line above creates a `ZkPropStringField` element and adds it to the form.

Behaviour of property based fields:

- When the user changes the value of an input, the value in the property also changes.
- The form validates its BO after each change. Validation provides user feedback automatically.
- Changing the BO value **does not change** the UI field content. Use the `value` of the field for the change.

### Value Based Fields

Value based fields are not linked to any properties, you provide a value and handle changes yourself.

```kotlin
+ stringField("initial content") onChange { value -> toastSuccess { "new value: $value" } }
```

The helper functions follow the simple pattern `<dataType>Field`. Check the recipe
below for an example.

Behaviour of value based fields:

- When the user changes the input, the field calls the callback passed to `onChange`.
- The form still validates the BO after each change.

Recipe: [Form With Custom Fields](/doc/cookbook/browser/field/custom/recipe.md)

### Built-In Fields [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/form/FormBuiltinExample.kt)

This form shows all built in field types.

<div data-zk-enrich="FormBuiltinExample"></div>

### Find Fields

Use the `find` extension function to find the field that belongs to a given
property easily.

This function returns with `ZkFieldBase` type, so you have to use `as` if 
you need field type specific access.

```kotlin
(bo::intValue.find() as ZkIntField).value = 15
```

### Save As

Convenience functions provided to save the field into a variable or a property easily:

```kotlin
class Form : ZkForm() {
    
    lateinit var intValue : ZkIntField
    
    override fun onCreate() {
        
        + bo::intValue saveAs ::intField
        
    }
}
```

```kotlin
class Form : ZkForm() {
    
    override fun onCreate() {
    
        lateinit var intValue : ZkIntField
   
        + bo::intValue saveAs { intValue = it }
   
    }
}
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
- EntityId, EntityId?  
- Enum, Enum?
- Int, Int?
- Long, Long?
- Double, Double?
- Instant, Instant?
- Secret, Secret?
- String, String?
- UUID, UUID?

#### Select For References

To add a select for an `EntityId` property use the code below. This
results in a select element on the UI.

The block after `query` is a suspend function that is used to get the 
items to shown in the select.

```kotlin
+ bo::referenceField query { /* select data */ }
```

To include all possible referenced entities:

```kotlin
+ bo::recordSelectValue query { ExampleReferenceBo.all().by { it.name } }
```

#### Select For Strings

To add a select for `String` and `String?` fields, use the `asSelect` 
transform function.

The block after `query` is a suspend function that is used to get the
items to shown in the select.

```kotlin
+ bo::stringSelectValue.asSelect() options { listOf("option 1", "option 2", "option3") }
```

#### Select With Radio Buttons

Use the `asRadioGroup` transform function to change a select form the usual dropdown
into a list of radio buttons.

`asRadioGroup` supports:

- Enum
- Enum?  
- String
- String?

```kotlin
+ bo::enumSelectValue.asRadioGroup()
```

Recipe: [Select As Radio Buttons](/doc/cookbook/browser/form/select/radio/recipe.md)

#### Select With Option Filter

When the `filter` option of a dropdown select is true, the user can filter the
options by typing in characters.

```kotlin
+ bo::recordSelectValue query ::queryRecords saveAs ::recordSelectField filter true
```

<div data-zk-enrich="SelectWithFilter"></div>

Recipe: [Select With Filter](/doc/cookbook/browser/field/select/filter/recipe.md)

#### Constant String

To add a constant string as a field, use the `constString` function:

```kotlin
+ constString("label") { "value" }
```

#### Optional Boolean

To add an optional boolean property use the `opt` function. This function
lets the user select from three values:

- not selected
- true
- false

You can customize the true and false texts with the function parameters.

```kotlin
+ opt(bo::optBooleanValue, stringStore.trueText, stringStore.falseText)
```

#### TextArea

To use the `asTextArea` transform function for a string field to convert it
into a text area.

```kotlin
+ bo::textAreaValue.asTextArea()
```

To set the inner area height:

```kotlin
+ bo::textAreaValue.asTextArea { area.style.height = 40.px }
```

### Customization

#### label

Use the `label` configuration function to specify a customized label:

```kotlin
+ textarea(bo::textAreaValue) label "My Label"
```

#### readOnly

Use the `readOnly` configuration function to set a field read-only:

```kotlin
+ bo::name readOnly true
```

<div data-zk-enrich="ReadOnlyBuiltinForm"></div>

#### submitOnEnter

Use the `submitOnEnter` configuration function on any ZkStringBase fields
to let the user submit the form with Enter.

```kotlin
+ bo::password submitOnEnter true
```

#### newSecret

Use the `newSecret` configuration function to set autocomplete of secret fields to `new-password`.

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

## Field Value Change

### Change Callback

Each field have an `onChangeCallback` property that may store a function. The field
executes this function when the value of the field changes and is not an `invalidInput`.

The `onChange` and `onChange3` transform functions set `onChangeCallback` when applied to a 
field. You can also set the `onChangeCallback` property directly.

- `onChange` has one parameter: the changed value
- `onChange3` has three parameters: origin of the change, the value and the field itself

Origin of the change is a [ChangeOrigin](/core/core/src/jsMain/kotlin/zakadabar/core/browser/field/ChangeOrigin.kt).
This enumeration has two values `User` and `Code`. 

- `User` means the origin of the change is a browser event
- `Code` means the origin of the change is an assignment to the `value` property

**Note**

1. The user changes a field with a callback.
2. The callback changes another field with a callback.
3. Origin of the second callback is `Code`.

```kotlin
+ bo::booleanValue onChange ::onBooleanChange
```

```kotlin
+ bo::stringValue onChange3 { origin, value, field -> toastSuccess { value } }
```

Example: [Field Change Event]/doc/cookbook/browser/field/onchange/recipe.md

### Field Value

Each field has a `value` and a `valueOrNull` property. These can be used to get 
or set the value of the field.

Assigning to the `value` property:

- changes the value in the BO (except constant fields)
- updates the UI
- calls `ZkFieldContext.validate`
- calls `onChangeCallback` with `ChangeOrigin.Code`
- 
Reading the `value` property:

- throws `IllegalStateException` when `invalidInput` is true
- throws `NoSuchElementException` when the field is not set

```kotlin
+ bo::intValue saveAs ::intField

+ buttonPrimary("Change intValue to 15") {
    intField.value = 15
}
```

The `valueOrNull` property may be uses to read the value without exceptions
thrown. 

`valueOrNull` does not allow writes to prevent null pointer exceptions (it
has a protected setter available only for classes that extend `ZkFieldBase`).


### Write a Field

You can easily write your own form field types by extending
[ZkFieldBase](/core/core/src/jsMain/kotlin/zakadabar/core/browser/field/ZkFieldBase.kt).

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

## Images And Attachments

Use [ZkImageField](/lib/blobs/src/jsMain/kotlin/zakadabar/lib/blobs/browser/image/ZkImagesField.kt) 
and [ZkAttachmentsField](/lib/blobs/src/jsMain/kotlin/zakadabar/lib/blobs/browser/attachment/ZkAttachmentsField.kt) 
to provide image or attachment handling.
