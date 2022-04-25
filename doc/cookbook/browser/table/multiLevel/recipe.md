# Multi Level Table

```yaml
level: Intermediate
targets:
  - browser
tags:
  - table
```

To have row groups:

- set  `multiLevel` to `true` in `onConfigure`
- add a `ZkLevelColumn` column to the table
- override the `getRowLevel` function

```kotlin
override fun onConfigure() {
    multiLevel = true
    + ZkLevelColumn(this)
}

override fun getRowLevel(row : ZkTableRow<T>): Int {
    return 0    
}
```

<div data-zk-enrich="TableMultiLevel"></div>

## Guides

- [Tables](/doc/guides/browser/builtin/Tables.md)

## Code

- [TableMultiLevel](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/multiLevel/TableMultiLevel.kt)
