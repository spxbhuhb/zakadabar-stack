# Basic Crud: Browser

```yaml
level: Beginer
targets:
  - browser
tags:
  - crud
  - table
  - form
```

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="Bender">

You can use the [Bender](/doc/guides/tools/Bender.md) to generate the source codes for CRUD.
</div>

To write a CRUD:

- use [ZkCrudTarget](/core/core/src/jsMain/kotlin/zakadabar/core/browser/crud/ZkCrudTarget.kt) for a full page crud
- use [ZkInlineCrud](/core/core/src/jsMain/kotlin/zakadabar/core/browser/crud/ZkCrudTarget.kt) for an inline crud
- write a table 
- write a form

## Example

<div data-zk-enrich="BasicCrud"></div>

## Guides

- [Crud](/doc/guides/browser/builtin/Crud.md)
- [Tables](/doc/guides/browser/builtin/Tables.md)
- [Forms](/doc/guides/browser/builtin/Forms.md)

## Code

- [BasicCrud.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/crud/basic/BasicCrud.kt)
- [BasicInlineCrud.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/crud/basic/BasicInlineCrud.kt)
- [BasicInlineCrudForm.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/crud/basic/BasicInlineCrudForm.kt)
- [BasicInlineCrudTable.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/crud/basic/BasicInlineCrudTable.kt)
