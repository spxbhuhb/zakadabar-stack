# Table with counter

```yaml
level: Beginner
targets:
  - browser
tags:
  - table
  - counter
```

To add a counter to the bottom of the table, set the `counter` variable to true. The counter has 3 parts:
 - counter title is text before the count number
 - all count is the size of the unfiltered data. The default value is the size of the full data at the first onResume. To change this, set the `allCount` variable.
 - filtered count the size of the filtered (currently visible) data



<div data-zk-enrich="TableWithCounter"></div>

## Guides

- [Tables](/doc/guides/browser/builtin/Tables.md)

## Code

- [Table with counter](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/counter/TableWithCounter.kt)