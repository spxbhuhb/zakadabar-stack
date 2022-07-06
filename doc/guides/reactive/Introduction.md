# Reactive: Introduction

## Basics: Components

The basic building blocks of a Reactive application are components. The 
following code defines the "HelloWorld" component which simply displays the
text "Hello World!".

```kotlin
@Reactive
fun HelloWorld() {
    Text("Hello World!")
}
```

## Basics: State

Each component has a *state*. When this state changes the component automatically
updates the UI to reflect the change. 

Defined variables are part of the component state, and they are *reactive by default*.

```kotlin
@Reactive
fun Counter() {
    var count = 0
    Button { "Click count: $count" } onClick { count++ }
}
```

The state of this component consists of the `counter` variable. Whenever you
click on the button, the `counter` is incremented. The component realizes that
the `counter` has been changed and updates the UI.

## Basics: Parameters

You can add parameters to the component. Parameters are parts of the
component state.

```kotlin
@Reactive
fun Counter(label : String) {
    var count = 0
    Button { "$label: $count" } onClick { count++ }
}
```

## Basics: Nesting

Components may contain other components, letting you build complex UI
structures. When `count` of the parent component changes Reactive
automatically updates the child component.

```kotlin
@Reactive
fun Child(count : Int) {
    Text("Click count: $count")
}

@Reactive
fun Parent() {
    var count = 0
    Child(count) onClick { count++ }
}
```

## Basics: Conditions

You can use the standard Kotlin `if` and `when` to decide what to display.

```kotlin
@Reactive
fun Counter() {
    var count = 0
    Button { "click count: $count" } onClick { count ++ }
    if (count < 3) {
        Text("(click count is less than 3)")
    }
}
```

```kotlin
@Reactive
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
@Reactive
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
@Reactive
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
@Reactive
fun Wrapper(@Reactive block : () -> Unit) {
    Text("before the block")
    block()
    Text("after the block")
}

@Reactive
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
@Reactive
fun Use() {
    Wrapper {  } name "Wrapped Block"
}
```

This inner transform sets the `name` state variable of `Wrapper` to "Wrapped block".

```kotlin
@Reactive
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
@Reactive
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
@Reactive
fun Comp(@Reactive block : CompTransformScope.() -> Unit) {
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