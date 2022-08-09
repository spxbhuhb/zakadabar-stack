# Overview

## Entry

Entry points (calls of the `rui` function):

1. `RuiFunctionVisitor` transforms the `block` parameter:
   1. into a Rui class named `RuiRootX` where `X` is the start offset of the block
   2. into a function that:
      1. calls `RuiAdapterRegistry.adapterFor` to get an adapter, parameters of the `rui` function are passed to `adapterFor`
      2. creates a new `RuiRootX` instance with the adapter

## With Trace

When *with trace* is enabled, the plugin adds tracing code to the compiled classes.

Trace guidelines:

- Add trace in a separate function that starts with a check of `ruiContext.withTrace` and returns immediately when it is false.
- Do not add/alter any live functionality in these trace functions.
- Be non-intrusive, do not change the state of the component in any ways. Getters may change the state!