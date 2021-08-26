# Next

This page contains the changes included in the next release.

## Core

added:

- `ZkCssStyleSheet.onConfigure` hook for last minute style changes
- `ZkTable.styles` property to provide style customization per table
- `cssClass(on:()->String, builder)` convenience for building complex css class selectors

changed:

- `ZkCssStyleRule` and its functions are now open
- `ZkCssStyleRule.selector` is a function instead of a `String`
- `ZkTableStyles` values are now open, moved sub-classes out of `table`

fixed:

- Search in uppercase #61

## Cookbook

added:

- Vertical Table Cell Border

## Site

fixed:

- broken build script links