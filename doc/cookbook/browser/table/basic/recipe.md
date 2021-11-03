# Basic Table

```yaml
level: Beginner
targets:
  - browser
tags:
  - table
```

To write a basic table, extend the [ZkTable](/core/core/src/jsMain/kotlin/zakadabar/core/browser/table/ZkTable.kt) class.

There are different ways to populate the data of the table:

- with a query (as the example shows below)
- directly with a list of rows by calling `setData` (see `BasicTableDirect` below)
- from a CRUD (check [Basic Crud](/doc/cookbook/browser/crud/basic/recipe.md) for details)

<div data-zk-enrich="BasicTable"></div>

## Guides

- [Tables](/doc/guides/browser/builtin/Tables.md)

## Code

- [BasicTable](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/basic/BasicTable.kt)
- [BasicTableDirect](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/basic/BasicTableDirect.kt)