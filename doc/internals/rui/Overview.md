# Overview

## Entry

Entry points (calls of the `rui` function):

1. `RuiFunctionVisitor` transforms the `block` parameter:
   1. into a Rui class named `RuiRootX` where `X` is the start offset of the block
   2. into a function that:
      1. calls `RuiAdapterRegistry.adapterFor` to get an adapter, parameters of the `rui` function are passed to `adapterFor`
      2. creates a new `RuiRootX` instance with the adapter

## Structural Fragments

Structural fragments are the ones the plugin creates implicitly from the Kotlin program structure:

* Block
* Branch
* Loop
* Call

## Anchor

We use anchors to add and remove rui fragments to/form the underlying UI implementation. Therefore,
type of the anchors and handling of anchors is specific to that implementation. `RuiFragment` and `RuiAdapter`
is a generic type to provide type safety for different UI implementations.

The first anchor, called "root anchor", of a rui fragment tree is created by the adapter. The root
anchor is passed to the `RuiRootX` instance of the [entry point](#Entry).

### HTML

#### Block

Blocks should not add DOM container elements as that would change the expected structure. In the following example
the block that contains `c1` and `c2` has to put those fragments directly under the grid to ensure that the browser
will place the components properly.

```kotlin
grid {
    c1()
    c2()
}
```

In this case the rendered HTML should be something like this.

```html
<div class="grid-class">
   <input id="from-c1">...</input>
   <button id="from-c2">...</button>
</div>
```

Structure of blocks is permanent. Each fragment is present all the time.

#### Branch

Branches put one fragment into the structure. The `ruiSelect` function shall return a placeholder
instance when there is no user-defined fragment selected. 

When the branch condition changes and `ruiSelect` returns with a new instance, the current fragment
(or placeholder) is replaced with the new one.


## With Trace

When *with trace* is enabled, the plugin adds tracing code to the compiled classes.

Trace guidelines:

- Add trace in a separate function that starts with a check of `ruiContext.withTrace` and returns immediately when it is false.
- Do not add/alter any live functionality in these trace functions.
- Be non-intrusive, do not change the state of the component in any ways. Getters may change the state!