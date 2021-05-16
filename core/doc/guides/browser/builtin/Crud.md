# Crud

[ZkCrudTarget](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/ZkCrudTarget.kt)
is a binding class that provides easy configuration of CRUD routing on the frontend.

## Write a CRUD  [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/crud/SimpleExampleCrud.kt)

1. [Write a Record DTO](../../common/Data.md#Write-a-Record-DTO)
1. [Write a Form](./Forms.md#Write-a-Form)
1. [Write a Table](./Tables.md#Write-a-Table)
1. Bind it all together with a [ZkCrudTarget](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/ZkCrudTarget.kt) see code below.
1. [Route the CRUD](../browser/builtin/Crud.md#Route-a-CRUD)
1. [Include the CRUD](../browser/builtin/Crud.md#Include-a-CRUD)

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

## Route a CRUD

Add the CRUD the routing. See [Routing](../structure/Routing.md#Write-a-Routing) for details.

```kotlin
+ SimpleExampleCrud
```

## Include a CRUD

Add the crud to your sidebar. See [SideBar](../builtin/SideBar.md) for details.

```kotlin
+ item(SimpleExampleCrud)
```
