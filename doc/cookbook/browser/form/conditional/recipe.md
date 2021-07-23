# Conditional Form Field Addition

## Use Case

- You have quite similar forms at different places. 
- However, not exactly the same.
- Fields are shown / hidden depending on where the form is.
- Some fields are read only here, writable there, not shown at all elsewhere.

## Implementation

Use the `readOnly`, `writable` and `excluded` fields of `ZkForm`.

This component shows the same form with different configurations.

<div data-zk-enrich="ConditionalFormExample"></div>

## Guides

[Forms](/doc/guides/browser/builtin/Forms.md)

## JavaScript 

[ConditionalForm.kt](/cookbook/src/jsMain/kotlin/zakadabar/cookbook/browser/form/conditional/ConditionalForm.kt)
