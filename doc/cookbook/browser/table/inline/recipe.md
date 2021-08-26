# Inline Table Edit

```yaml
level: Beginner
targets:
  - browser
tags:
  - table
  - inline
  - edit
```

Ways to add editable fields to the table:

- edit a field of the BO shown by the table
- add an "extension" field which is not in the BO

## Use a BO Field

Add a custom column as in [TableEditInline](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/inline/TableEditInline.kt).

The values are written directly into the row BOs, accessible through `ZkTable.fullData`.

<div data-zk-enrich="TableEditInline"></div>

## Use an Extension Field

Add one of the extension columns as in [TableEditInlineNoBo](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/inline/TableEditInlineNoBo.kt). Pass an action that is called on data change, or use the
`values` map to check the field values.

Keys of the `values` map are the row ids as created by `ZkTable.getRowId`.

<div data-zk-enrich="TableEditInlineNoBo"></div>

## Guides

- [Tables](/doc/guides/browser/builtin/Tables.md)

## Code

- [TableEditInline](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/inline/TableEditInline.kt)
- [TableEditInlineNoBo](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/inline/TableEditInlineNoBo.kt)