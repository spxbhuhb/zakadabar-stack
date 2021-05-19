# Crud

CRUDs are advanced components that link the listing and editing of a DTO together. The stack provides
two classes for CRUD definition:

* [ZkCrudTarget](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/crud/ZkCrudTarget.kt) is a binding class 
  that provides easy configuration of CRUD routing on the frontend.
* [ZkInlineCrud](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/crud/ZkInlineCrud.kt)
  is an element to be included somewhere on a page, but it is not the page itself.

## Write a CRUD  [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/crud/SimpleExampleCrud.kt)

### Write With Kod-o-mat

The easiest way to write a CRUD is to use the [Kod-o-mat](/en/Kodomat) to generate the source code, then copy and paste.

### Write Manually

1. [Write a Record DTO](../../common/Data.md#Write-a-Record-DTO)
1. [Write a Form](./Forms.md#Write-a-Form)
1. [Write a Table](./Tables.md#Write-a-Table)

#### Write a CRUD Target

1. Bind it all together with a [ZkCrudTarget](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/crud/ZkCrudTarget.kt) see code below.
1. [Route the CRUD](#Route-a-CRUD-Target)
1. [Include the CRUD](#Include-a-CRUD-Target)

```kotlin
object SimpleExampleCrud : ZkCrudTarget<SimpleExampleDto>() {
    init {
        companion = SimpleExampleDto.Companion
        dtoClass = SimpleExampleDto::class
        pageClass = SimpleExampleForm::class
        tableClass = SimpleExampleTable::class
    }
}
```

#### Route a CRUD Target

Add the CRUD the routing. See [Routing](../structure/Routing.md#Write-a-Routing) for details.

```kotlin
+ SimpleExampleCrud
```

#### Include a CRUD Target

Add the crud to your sidebar. See [SideBar](../builtin/SideBar.md) for details.

```kotlin
+ item(SimpleExampleCrud)
```

#### Write an Inline CRUD

Use [ZkInlineCrud](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/crud/ZkInlineCrud.kt) to write an inline CRUD.

```kotlin
class BuiltinCrud : ZkInlineCrud<BuiltinDto>() {
    init {
        companion = BuiltinDto.Companion
        dtoClass = BuiltinDto::class
        pageClass = BuiltinForm::class
        tableClass = BuiltinTable::class
    }
}
```

These two examples are inline CRUDs. One for BuiltinDto and one for ExampleReferenceDto.
You need two because BuiltinDto contains a mandatory reference to ExampleReferenceDto and
to set that you need ExampleReferenceDto records.

CRUD of ExampleReferenceDto [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/crud/CrudRefrenceExample.kt)

<div data-zk-enrich="CrudReferenceExample"></div>

CRUD of BuiltinDto [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/crud/CrudBuiltinExample.kt)

<div data-zk-enrich="CrudBuiltinExample"></div>

## TimeLine

### Changes

* 2021.5.19
  * move crud related classes from `pages` to `crud`
  * introduce ZkInlineCrud