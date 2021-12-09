# Next

This page contains the changes included in the next release.

## Core

**added**

- `ZkValue*Field` classes for value based form fields
- `ZkForm` helper functions to add value based fields
- `options` transform function in `ZkForm` to provide lists for selects

**deprecated**

- most of `Zk*Field` names, replace with `ZkProp*Field`
- `ZkStringBase`, replace with `ZkStringBaseV2`
- old deprecations in `ZkForm` now changed to `ERROR` level

## Cookbook

**added**

- [Form With Custom Fields](/doc/cookbook/browser/field/custom/recipe.md)