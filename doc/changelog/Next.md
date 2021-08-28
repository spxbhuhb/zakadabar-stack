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
- `ZkFieldBase.onChangeCallback` called whenever bo value changes
- `ZkFieldBase.value` current value
- `ZkFieldBase.valueOrNull` current value or null
- `onChange` form field transform function
- `onChange3` form field transform function
- `BoSchema.constraintsOrNull`
- `ChangeOrigin` for browser fields to pass event origin
- `ZkForm.saveAs` property convenience version

changed:

- `ZkCssStyleRule` and its functions are now open
- `ZkCssStyleRule.selector` is a function instead of a `String`
- `ZkTableStyles` values are now open, moved sub-classes out of `table`
- all browser fields use `context.styles`
- move field related CSS classes from `zkFormStyles` to `zkFieldStyles`
- change BO primitive wrapper fields to `var`
- `ZkFormStyles` and `ZkTableStyles` extends `ZkFieldStyles`
- rework select styles to support table and form use cases
- `ZkFieldBase` now has a second type parameter, the field type itself

removed:

`dense` from `ZkFieldContext` (replace with `styles`)

fixed:

- Search in uppercase #61
- `ZkBooleanField` styles for read mode form
- `ZkSelectBase` styles for read mode form

## Cookbook

added:

- Vertical Table Cell Border
- Field Change Event

changed:

- Inline Table Edit

## Site

fixed:

- broken build script links