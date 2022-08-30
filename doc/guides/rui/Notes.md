# Notes

## Coding Conventions

In the compiler plugin source code function names follows these conventions:

- `init*` functions are called during instance initialization
- `build*` functions are called from `RuiClassBuilder.build` (maybe deeper in the chain), they do no return with anything
- `ir*` functions create and return with `IrElement` instances
- `trace*` functions add trace code when it is enabled in the plugin configuration

## Definitions

A *component* is a class that extends `RuiFragment` and has its own state. The
state may be empty. The compiler plugin generates components from *original functions*.
It is also possible to manually write a component, actually most low-level components
that interact with the underlying UI are written manually.

An *original function* is a function the UI programmer writes and is annotated with `@Rui`.
The compiler plugin turns original functions into a *components*.

A *structural* is a general building block used by *components* to build the structure of 
the rendered UI. These are pre-defined by the Rui runtime: `RuiBlock`, `RuiWhen`,
`RuiLoop`. Structurals do not have a state, they use the state of the component they are
defined in when deciding how to render the UI.

A *fragment* is *component* or a *structural*. Fragments are the building blocks of
the runtime Rui structure.

A *higher-order function* is a Rui function that gets a Rui function as a parameter.

A *parameter function* is a function passed as parameter to a *higher-order function*.

The *runtime* is the `hu.simplexion.zakadabar:rui-runime` module. This contains base
classes and interfaces. It is important that this is a "static" runtime, there is no
engine running in the background to perform updates.

## External Patch

External patch functions update the external state variables of a component.
They are call-site dependent as two different call sites may need two different
external patch functions. In the example below patching the two T1 components
requires two different computations.

```kotlin
@Rui
fun test(i : Int) {
    T1(i + 1)
    T1(i + 2)
}
```

Declaration of external patch functions could be done with:
 
- lambdas,
- functions defined in the component, their reference passed as a parameter.

Lambdas are a bit easier to define but on JVM they result on a new class generated.
That may raise the problem of long application startup, therefore it seems better 
to stick to function definitions.

Name of generated external patch functions is `ruiEpX` where `X` is the start offset
of the original function call the external patch belongs to.

```kotlin
fun ruiEp543(it : RuiT1) {
    if (ruiDirty0 and 2 != 0) { // 2 is the mask for `this.value`
        it.p0 = this.value * 2
        it.ruiInvalidate(1) // 1 is the mask of `it.p0`
    }
}
```

External patch of higher order functions and loop blocks is complicated. See
Higher-Order Functions for details.

## Higher-Order Functions

Handling of higher order functions may be very complex as the example below shows.

```kotlin
@Rui
fun h0(ph: Int, @Rui func: (pc: Int) -> Unit) {
    func(ph * 2)
    func(ph * 3)
}

@Rui
fun test(p0: Int) {
    ho(p0) { p1 ->
        ho(p1) { p2 ->
            ho(p2) { p3 ->
                T1(p0 + p1 + p2 + p3)
            }
        }
    }
}
```

Consider the followings:

1. Higher-order components may perform calculations needed to properly patch the components deeper in the tree.
2. Components lower in the tree may access state variables higher in the tree.
3. A higher-order component may use the parameter function more than once.

The key insight here is that the parameter functions cannot extend the state. As they are part of the rendering,
they cannot define new state variables, therefore the states we are working here is static, it is defined by the
parameters of the parameter functions.

The parameter functions implicitly define components with:

- an external state, defined by the parameters of the parameter function (empty when there are no parameters),
- an empty internal state.

We use classes from the runtime to create instances of these implicit components:

- RuiImplicit0
- RuiImplicit1
- RuiImplicitN

The number in the class name is the number of state variables the class stores. The first two should cover
most of the use cases why the last may be used for any number of parameters.

To access the state stored in implicit instances we use the `RuiFragment.parent`.

Example:

```kotlin
@Rui
fun h0(ph: Int, @Rui func: (pc: Int) -> Unit) {
    func(ph * 2)
}

@Rui
fun test(value: Int) {
    ho(value) { p1 ->
        T1(value + p1)
    }
}
```

```kotlin
class RuiImplicit1<BT, VT, FT : RuiFragment<BT>>(
    override val ruiParent : RuiFragment<BT>,
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiExternalPatch : (it : RuiFragment<BT>) -> Unit,
    var v0: VT
) : RuiFragment {

    var inner: FT? = null

    override fun ruiPatch() {
        inner?.let {
            it.ruiExternalPatch()
            it.ruiPatch()
        }
    }
}
```

```kotlin
class RuiHo(
    override val ruiAdapter: RuiAdapter<BT>,
    override val ruiExternalPatch : (it : RuiFragment<BT>) -> Unit,
    var ph : Int,
    var builder : (syn : RuiSynFragment1<BT,Int,*>) -> RuiFragment<BT>
) : RuiFragment<BT> {
    
    val fragment : RuiFragment<BT>
    
    fun ruiGep0(it : RuiFragment<BT>) {
        it.v0 = this.ph0 * 2
    }
    
    init {
        fragment = RuiSynFragment1(
            ruiAdapter,
            ::ruiGep0,
            this.ph0 * 2
        ).also {
            inner = builder(it)
        }
    }
}
```

```kotlin
class RuiTest<BT> : RuiGeneratedFragment<BT> {

    var value: Int = 1

    fun ruiEp123(it: RuiH0) {
        it.ph = this.value
    }
    
    fun ruiEp456(it : RuiT1) {
        it.p0 = this.value + it.scope.v1
    }
    
    fun ruiBuilder1(syn : RuiSynFragment1<BT,Int,*>): RuiFragment<BT> =
            RuiT1(ruiAdapter, ::ruiGep123, this.value + syn.v1)

    override val fragment: RuiFragment

    init {
        fragment = RuiHo(ruiAdapter, ::ruiGep456, ::ruiBuilder1, p0)
    }
}
```

## Bridge

The bridge connects Rui fragments with their representation in the underlying
UI. Low-level fragments (those that directly interact with the actual UI)
typically implement the `RuiBridge` interface and transform their internal
state into an actual UI state.

The `BT` type parameter of the bridge is a type in the underlying UI, typically
ancestor of all UI elements, such as `Node` in HTML or `View` in Android.

`ruiMount` and `ruiUnmount` functions get the bridge of the parent fragment
and use `add` and `remove` methods to add and remove themselves. Some bridges
also implement the `replace` method which makes it possible to replace a
fragment with another in place. This is used by `if` and `when`.

### Bridge dependent and bridge independent fragments

A bridge independent fragment is one that does not depend on the actual type
its bridge uses. On the other hand, a bridge dependent fragment is one
that uses the bridge in some very specific manner.

For example `RuiBlock` is a bridge independent fragment. It does not care
about what goes on, it just has a few children, and they will handle the
bridging themselves.

`Text` fragments are typically bridge dependent because each platform has its
own way to add constant text to the UI. In browsers for example you use
`document.createTextNode`.

Bridge independent fragments use type parameter for the bridge receiver type:

```kotlin
open class RuiBlock<BT>(
    override val ruiAdapter: RuiAdapter<BT>,
    vararg val fragments: RuiFragment<BT>
) : RuiFragment<BT> {
    // ...
}
```

Fragments generated by the plugin are bridge independent, thus usable with any
kind of adapter/bridge.

## Placeholders

A placeholder is an anchor the fragment uses to add/remove its children. We cannot
just add and then replace fragments because it is possible that the selected
fragment is a block or a loop. Those add an unknown number of children, thus simple
replace is impossible. For browsers the placeholder may be a simple `Node`
(Svelte uses a `Text`), for Android an actual Placeholder view exists.

Placeholders are created by the `RuiAdapter.createPlaceholder` function.