# Rui: Introduction

Rui, short for Reactive UI, is a Kotlin compiler plugin that you may use to easily
build error-free user interfaces.

Rui is inspired by [Svelte](https://svelte.io), but it is not a port of Svelte.

## Basics: Components

The basic building blocks of a Rui application are components. The 
following code defines the "HelloWorld" component which simply displays the
text "Hello World!".

```kotlin
@Rui
fun HelloWorld() {
    Text("Hello World!")
}
```

## Basics: State

Each component has a *state*. When this state changes the component automatically
updates the UI to reflect the change. 

Defined variables are part of the component state, and they are *reactive by default*.
We call these *internal state variables*.

```kotlin
@Rui
fun Counter() {
    var counter = 0
    Button { "Click count: $counter" } onClick { counter++ }
}
```

The state of this component consists of the `counter` variable. Whenever you
click on the button, the `counter` is incremented. The component realizes that
the `counter` has been changed and updates the UI.

## Basics: Parameters

You can add parameters to the component. Parameters are parts of the
component state. We call these *external state variables*.

You cannot change the external state variables from the inside of
the component. However, it may happen that the parameter changes
on the outside. In that case the component updates the UI automatically.

```kotlin
@Rui
fun Counter(label : String) {
    var counter = 0
    Button { "$label: $counter" } onClick { counter++ }
}
```

## Basics: Boundary

Rui components have two main areas: *state initialization* and *rendering*.
These are separated by the *boundary*. 

Above the boundary, you initialize the component state. This is a one-time
operation, executed when the component is initialized.

Below the boundary, you define how to render the component. This part
is executed whenever the state changes.

**Very important** you cannot define variables, functions etc. in the 
*rendering* (except in event handlers, see later). This is a design decision we've 
made to avoid confusion.

Rui automatically finds the *boundary*: the first call to another Rui component
function marks the *boundary*.

```kotlin
@Rui
fun Counter() {
    var counter = 0
    // ---- boundary ----
    Button { "Click count: $counter" } onClick { counter++ }
}
```

## Basics: Event Handlers

*Event handlers* are functions called when something happens: user input,
completion of a launched co-routine etc.

Event handlers may change state variables and these changes result in a UI update.

You can define event handlers as local functions or as lambdas. Rui recognizes
when a block changes a state variable and automatically updates the UI.

The handlers in this example are equivalent. Note that whichever button you
click, labels of all show the new counter value.

```kotlin
@Rui
fun Counter() {
    var counter = 0
    
    fun increment() {
        counter++
    }
    
    Button { "Click count: $counter" } onClick ::increment
    Button { "Click count: $counter" } onClick { counter++ }
}
```

## Basics: Nesting

Components may contain other components, letting you build complex UI
structures. When `counter` of the parent component changes the child
automatically updates the child component.

```kotlin
@Rui
fun Child(counter : Int) {
    Text("Click count: $counter")
}

@Rui
fun Parent() {
    var counter = 0
    Child(counter) onClick { counter++ }
}
```

## Basics: Conditions

You can use the standard Kotlin `if` and `when` to decide what to display.

```kotlin
@Rui
fun Counter() {
    var count = 0
    Button { "click count: $count" } onClick { count ++ }
    if (count < 3) {
        Text("(click count is less than 3)")
    }
}
```

```kotlin
@Rui
fun Counter() {
    var count = 0
    when (count) {
        1 -> Text("click count: 1")
        2 -> Text("click count: 2")
        else -> Text("click count > 2")
    }
    Button { "click to increment" } onClick { count ++ }
}
```

## Basics: For Loops

You can use the standard Kotlin `for` loops.

```kotlin
@Rui
fun Counter() {
    var count = 0
    Button { "click count: $count" } onClick { count ++ }
    
    for (i in 0 until count) {
        Text("click.")
    }
}
```

## Basics: While Loops

You can use the standard Kotlin `while` and `do-while` loops, but you have to be 
careful. If you use a variable that is part of the component state, the while 
loop will increase that variable *in the state*.

This means that you have to change that variable to update the content of
the loop and the content will be updated based on the current value of the 
variable.

To overcome this, you can use the `stateless` helper function. This function
tells the compiler that the variable is not part of the component state,
it is used only temporarily.

```kotlin
@Rui
fun Counter() {
    var count = 0
    Button { "click count: $count" } onClick { count ++ }

    var i = stateless { 0 }
    
    while (i < count) {
        i++
        Text("click.")
    }
}
```

## Basics: Higher Order Components

Higher order components have a function parameter which in turn is another
component.

```kotlin
@Rui
fun Wrapper(@Rui block : () -> Unit) {
    Text("before the block")
    block()
    Text("after the block")
}

@Rui
fun Counter() {
    var count = 0
    Wrapper {
        Button { "click count: $count" } onClick { count ++ }
    }
}
```

## Transforms: Intro

Transforms are little helper functions that let you change the state of a 
component with clear, easy to read code.

There are two types of transforms: outer and inner. For both the transformed 
component has to support the transformation (more about that later).

This outer transform sets the `name` state variable of `Wrapper` to "Wrapped Block".

```kotlin
@Rui
fun Use() {
    Wrapper {  } name "Wrapped Block"
}
```

This inner transform sets the `name` state variable of `Wrapper` to "Wrapped block".

```kotlin
@Rui
fun Use() {
    Wrapper {
        name = "Wrapped Block"
        Button { "click count: $count" } onClick { count ++ }
    }
}
```

You do not have to write any transforms, they are purely for convenience.
That said, most components in the library provide these because we like
convenience.

## Transforms: Outer

The definition of an outer transform is a bit of cumbersome, worth it only when 
you use the component many times. 

`CompTransformScope` is the object we use to scope the transforms into this
component. You don't have to worry about it much, it is used only to
indicate which type of component are we transforming.

To write an outer transform for a component:

1. create a transform scope object (`CompTransformScope`)
2. return with this object from the component function
3. define the transform function (`CompTransformScope.value`)

```kotlin
@Rui
fun Comp() : CompTransformScope {
    var name = "block"
    Text("the name is $name")
    return CompTransformScope
}

object CompTransformScope

@Transform
infix fun CompTransformScope.value(v : String) = Unit
```

## Transforms: Inner

For higher order functions you can define inner transforms by defining the
block passed as receiver function on a transform object. 

These let you set state variables of the higher order function inside the block
passed as parameter.

To make an inner transform for a component:

1. create a transform scope object (`CompTransformScope`)
2. use the scope as the receiver of the parameter function (`block`)
3. add the transformed variable into the transform scope (`CompTransformScope.name`)

```kotlin
@Rui
fun Comp(@Rui block : CompTransformScope.() -> Unit) {
    var name = "block"
    Text("before the $block")
    CompTransformScope.block()
    Text("after the $block")
}

@Transform
object CompTransformScope {
    var name : String = ""
}
```