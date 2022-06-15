# Internals

## Transform Algorithm

The `reactive` plugin:

- creates a reactive component class for each function annotated with `@Reactive`
- creates a reactive data class for each class annotated with `@Reactive`
- transforms the calls to reactive functions into the component management code

### Reactive Component Classes

The plugin creates a class named `Reactive$<name-of-function>` for each function
annotated with `@Reactive`.

Also, each call to reactive functions creates a reactive component instance (a
child component). The parent component manages the child:

- create it during initialization of the parent
- patch it when necessary
- dispose it when the parent component is disposed

The plugin generates the code for child component management.

```kotlin
@Reactive
fun A(value : Int) {
    B(value + 2) // B is also a reactive component    
}

@Reactive
fun B(value : Int) {
    // ...
}
```

is transformed into:

```kotlin
class ReactiveA(
    var value : Int
) : ReactiveComponent() {

    val b0 = ReactiveB(value + 2)

    override fun patch(mask : Array<Int>) {
        dirty = mask // save the mask for higher-order functions (more about this below)
        if (mask[0] and 1 != 0) { // if A.value has been changed
            b0.value = value + 2 // calculate and set B.value
            b0.patch(arrayOf(1)) // patch B, so it will apply the new value wherever it is necessary
        }
    }
    
    override fun dispose() {
        b0.dispose()
    }
}
```

### Parameter Variables

```kotlin
fun A(p1 : String, p2 : Int) {
    // ...
}
```

Is transformed into:

```kotlin
fun A(p1 : String, p2 : Int) {
    var `p1$0` = p1 // mask = 1
    var `p2$0` = p2 // mask = 2
}
```

#### Details

This strategy means an overhead but it is necessary. Let's analyse the following code fragment:

```kotlin
@Reactive
fun A() {
    var counter = 0
    B(counter)
    onClick { counter++ }
}

@Reactive
fun B() {
    Text(counter)
}
```

B is compiled into something like this, introducing an internal variable for the counter.

```kotlin
fun B(counter : Int) : Component {
    // ...
    var counter0 = counter
    // ...
}
```

We need a `var` instead of `val` because in case the origin variable changes in
`A` the patch method of `B` will be called. That patch method has to get the
value from somewhere and that somewhere has to be writable.

Higher order functions makes data handling even more complex, as the blocks passed
to the higher order function has to reach up to the original closure to access
the data.

Svelte uses a `ctx` to store the data but in Kotlin a general array wouldn't
keep the types. So, it seems like a better idea to define the variables one-by-one.

### Framework Variables

The following framework variable declarations are added to the body:

```kotlin
val `self$0` = Component()
val `dirty$0` = arrayOf(0)
```

`self$0` is the `Component` instance that holds the closure together and stores
the component methods, so they can be accessed from outside the closure.

`dirty$0` is mask that represents which variables of the component changed.
The patch method uses dirty to patch the component with the changed variables.
Patch also clears dirty.

#### Details

We store the dirty mask in the closure to let the blocks passed to higher order
functions access it easily. Consider the following code:

```kotlin
@Suppress("CanBeVal")
fun A() {
    var v0 = 1
    B {
        var v1 = 2
        C {
            var v2 = 3
            D(v0, v1, v2)
        }
    }
}
```

We have to call `D.patch` whenever any of the variables change. This is not
trivial because there are two higher order functions between the closure of A 
and the closure C. Instead of trying to pass down the dirty masks in the 
patch calls we've decided to use the closures.

The code above is compiled so that each closure has its own dirty mask variable:

- `dirty$0` for A
- `dirty$1` for the block passed to B
- `dirty$2` for the block passed to C
- `dirty$3` for D

So, the block passed to C can actually check:

- `dirty$0` to see if `v0` has been changed
- `dirty$1` to see if `v1` has been changed
- `dirty$2` to see if `v2` has been changed

Then it has to update the appropriate 

