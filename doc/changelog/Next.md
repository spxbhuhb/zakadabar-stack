# Next

This page contains the changes included in the next release.

## Core

added:

- `ZkCssStyleSheet.onConfigure` hook for last minute style changes
- `ZkTable.styles` property to provide style customization per table
- `cssClass(on:()->String, builder)` convenience for building complex css class selectors
- `ZkBooleanExtensionColumn`
- `ZkStringExtensionColumn`
- add `styles` to `ZkFieldContext`
- `ZkTable` calls `ZkColumn.onTableSetData` from `setData`

changed:

- `ZkCssStyleRule` and its functions are now open
- `ZkCssStyleRule.selector` is a function instead of a `String`
- `ZkTableStyles` values are now open, moved sub-classes out of `table`
- all browser fields use `context.styles`
- move field related CSS classes from `zkFormStyles` to `zkFieldStyles`
- change BO primitive wrapper fields to `var`
- `ZkFormStyles` and `ZkTableStyles` extends `ZkFieldStyles`
- rework select styles to support table and form use cases


removed:

`dense` from `ZkFieldContext` (replace with `styles`)

fixed:

- Search in uppercase #61

## Cookbook

added:

- Vertical Table Cell Border

## Site

fixed:

- broken build script links