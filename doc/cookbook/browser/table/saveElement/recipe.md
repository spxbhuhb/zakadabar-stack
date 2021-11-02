# Preserve Table State

```yaml
level: Advanced
targets:
  - browser
tags:
  - table
  - save
  - restore
  - state 
```

This recipe shows how to save a table element and reuse it later in the exact same
configuration, preserving even the scroll position.

This technique preserves everything, including:

- preloaded data
- actual data of the table
- filtering
- sort operations performed before
- scroll position

The table itself does not know about being preserved, the element that contains the
table and the editors that modify table data work together to keep the table state
consistent.

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Careful!">
Make sure that you read and understand the comments in the example code.

This is a somewhat advanced example, don't just cut & paste, it won't work.
</div>


<div data-zk-enrich="TableSaveElement"></div>

## Guides

- [Tables](/doc/guides/browser/builtin/Tables.md)

## Code

- [TableSaveElement](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/saveElement/TableSaveElement.kt)
