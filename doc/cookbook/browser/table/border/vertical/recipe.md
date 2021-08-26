# Vertical Table Cell Border

```yaml
level: Beginner
targets:
  - browser
tags:
  - table
  - border
  - css
```

1. Extend [ZkTableStyles](/core/core/src/jsMain/kotlin/zakadabar/core/browser/table/zkTableStyles.kt).
1. Use `onConfigure` or override the `cell` class and add a new `lastHeaderCell` class.
1. Apply your new style in the given scope (see below).

<div data-zk-enrich="TableVerticalBorderSome"></div>

## Scope

### For All Tables

Assign an instance of your new table style sheet to `zkTableStyles` during application startup.

See [TableVerticalBorderSome](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/border/vertical/TableVerticalBorderSome.kt),
but instead of declaring the style variable `exampleStyles`, change `zkTableStyles.`

### For Some Tables

Override the `styles` variable of the table with your extended style or change it in `onConfigure` as in
[TableVerticalBorderSome](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/border/vertical/TableVerticalBorderSome.kt).

### For One Table

Use an anonymous style object as in [TableVerticalBorderOne](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/border/vertical/TableVerticalBorderOne.kt).

## Guides

- [Themes, Css](/doc/guides/browser/structure/ThemesCss.md)
- [Tables](/doc/guides/browser/builtin/Tables.md)

## Code

- [TableVerticalBorderSome](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/border/vertical/TableVerticalBorderSome.kt)
- [TableVerticalBorderOne](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/border/vertical/TableVerticalBorderOne.kt)