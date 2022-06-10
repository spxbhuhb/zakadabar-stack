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

With scroll:

<div data-zk-enrich="TableVerticalBorderScroll"></div>

Without scroll:

<div data-zk-enrich="TableVerticalBorderNoScroll"></div>

## Scope

### For All Tables

Assign an instance of your new table style sheet to `zkTableStyles` during application startup.

See [TableVerticalBorder](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/border/vertical/TableVerticalBorder.kt),
but instead of declaring the style variable `exampleStyles`, change `zkTableStyles.`

### For Some Tables

Override the `styles` variable of the table with your extended style or change it in `onConfigure` as in
[TableVerticalBorder](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/border/vertical/TableVerticalBorder.kt).

## Guides

- [Themes, Css](/doc/guides/browser/structure/ThemesCss.md)
- [Tables](/doc/guides/browser/builtin/Tables.md)

## Code

- [TableVerticalBorder](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/table/border/vertical/TableVerticalBorder.kt)
