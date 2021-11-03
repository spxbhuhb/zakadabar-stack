# Crud

CRUDs are advanced components that link the listing and editing of BOs together. The stack provides
two classes for CRUD definition:

* [ZkCrudTarget](/core/core/src/jsMain/kotlin/zakadabar/core/browser/crud/ZkCrudTarget.kt) is a binding class 
  that provides easy configuration of CRUD routing on the frontend.
* [ZkInlineCrud](/core/core/src/jsMain/kotlin/zakadabar/core/browser/crud/ZkInlineCrud.kt)
  is an element to be included somewhere on a page, but it is not the page itself.

## Write a CRUD  [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/crud/SimpleExampleCrud.kt)

### Generate With Bender

The easiest way to write a CRUD is to use the [Bender](../../tools/Bender.md) to generate the source code, then copy and paste.

### Write Manually

1. [Write an Entity BO](../../common/Data.md#Write-an-Entity-BO)
1. [Write a Form](./Forms.md)
1. [Write a Table](./Tables.md#Write-a-Table)
1. [Write a CRUD Target](#Write-a-CRUD-Target)

#### Write a CRUD Target

1. Extend [ZkCrudTarget](/core/core/src/jsMain/kotlin/zakadabar/core/browser/crud/ZkCrudTarget.kt) see code below.
1. [Route the CRUD](#Route-a-CRUD-Target)
1. [Include the CRUD](#Include-a-CRUD-Target)

```kotlin
class SimpleExampleCrud : ZkCrudTarget<SimpleExampleBo>() {
    init {
        companion = SimpleExampleBo.Companion
        dtoClass = SimpleExampleBo::class
        pageClass = SimpleExampleForm::class
        tableClass = SimpleExampleTable::class
    }
}
```

#### Route a CRUD Target

Add the CRUD to the routing. See [Routing](../structure/Routing.md#Write-a-Routing) for details.

```kotlin
+ SimpleExampleCrud()
```

#### Include a CRUD Target

Add the crud to your sidebar. See [SideBar](../builtin/SideBar.md) for details.

```kotlin
+ item<SimpleExampleCrud>()
```

#### Write an Inline CRUD

Use [ZkInlineCrud](/core/core/src/jsMain/kotlin/zakadabar/core/browser/crud/ZkInlineCrud.kt) to write an inline CRUD.

```kotlin
class BuiltinInlineCrud : ZkInlineCrud<BuiltinBo>() {
    init {
        companion = BuiltinBo.Companion
        dtoClass = BuiltinBo::class
        pageClass = BuiltinForm::class
        tableClass = BuiltinTable::class
    }
}
```

These two examples are inline CRUDs. One for BuiltinBo and one for ExampleReferenceBo.
You need two because BuiltinBo contains a mandatory reference to ExampleReferenceBo and
to set that you need ExampleReferenceBo records.

CRUD of ExampleReferenceBo [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/crud/CrudReferenceExample.kt)

<div data-zk-enrich="CrudReferenceExample"></div>

CRUD of BuiltinBo [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/crud/CrudBuiltinExample.kt)

<div data-zk-enrich="CrudBuiltinExample"></div>
