# Overview

## Entry

Entry points (calls of the `rui` function):

1. `RuiFunctionVisitor` transforms the `block` parameter:
   1. into a Rui class named `RuiRootX` where `X` is the start offset of the block
   2. into a function that:
      1. calls `RuiAdapterRegistry.adapterFor` to get an adapter, parameters of the `rui` function are passed to `adapterFor`
      2. creates a new `RuiRootX` instance with the adapter

