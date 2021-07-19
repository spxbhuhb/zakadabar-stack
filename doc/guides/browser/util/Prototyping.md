# Prototyping

To create frontend prototypes fast, use the [NYI](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/misc/NYI.kt)
element.

NYI is just a simple element with a message that act as a placeholder until you write the actual element:

```kotlin
    + NYI("this will be the header")
```

NYI has a left and right padding of `10px`.

## Example [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/misc/NYIExample.kt)

<div data-zk-enrich="NYIExample"></div>