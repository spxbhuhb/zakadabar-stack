# Next

This page contains the changes included in the next release.

## Core

**added**

- Optional Boolean column for table
- Optional Enum column for table
- getter based column builders for ZkTable
- `ZkColumn<T>.label` configuration function

**changed**

- ZkTable now uses "V2" versions of columns
    - "V2" uses getters instead of a properties
    - unaryPlus overrides pass the getter `{ row -> prop.get(row) }`  
    - we'll keep the old property-based classes for a while for compatibility
- `ZkTable.size` configuration function now works on `ZkColumn` instead of `ZkElement`

## Cookbook

**added**

- [Basic Crud: Browser](/doc/cookbook/browser/crud/basic/recipe.md)
- [Basic Table](/doc/cookbook/browser/table/basic/recipe.md)

**changed**

- rename "Export Custom Column" recipe to [Custom Table Column](/doc/cookbook/browser/table/customColumn/recipe.md)