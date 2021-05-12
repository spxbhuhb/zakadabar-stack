# Elements

The browser UI heavily uses the [ZkElement](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkElement.kt) class.
Almost all built-in components extend `ZkElement` because it provides convenient builtin functions.

ZkElement properties

| Property | Description |
| --- | --- |
| `id`  | ID of the ZkElement. All ZkElements have a unique, auto-generated id. |
| `element` | The HTML DOM node that belongs to this ZkElement.<br>The `id` of this DOM node is "zk-$id" where `id` is the value of the `id` property. |
| `lifeCycleState` | [ZkElementState](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkElementState.kt) state of the ZkElement.<br>Used to prevent un-intentional reuse of elements and to manage element swapping. |
| `childElements` | ZkElements that are child of this one. Child management functions are there to add / remove children easily. |
| `buildPoint` | The HTML DOM element that is currently built. This may change a lot during the build process as DIVs are usually added to build a visual layout. |

## Lifecycle

ZkElement has a simple lifecycle:

![lifecycle](./element-lifecycle.png)

We typically use `onCreate` to build the content of the element and use `onResume` to apply the current state when going
on-screen.

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Lifecycle Management">
ZkElement builder methods manage the lifecycle of children elements automatically. When you add an
element to another the lifecycles are synchronized.
</div>

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="onDestroy">
onDestroy is called only when you directly call ZkElement.clear,
ZkElement.clearChildren or ZkElement.onDestroy. It is not an all-purpose destructor.
This behaviour is intentional.
</div>

## Build

It is very common, that a frontend element has an internal layout with a lot of `div`s, other elements, etc. To support
this [ZkElement](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkElement.kt)
provides a number of builder methods.

### Operators [source code](../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/button/ButtonExamples.kt)

Operator overloads are provided to build structures idiomatically.

| Operator | Works On | Description |
| --- | --- | --- |
| `+` | String | Add text safely at the current build point, no need for escape. |
| `+` | String? | Add optional text safely at the current build point, no need for escape. |
| `!` | String | Add text unsafely at the current build point, sets innerHTML, **must** be properly escaped. |
| `+` | HTMLElement | Add an HTML element at the current building point. |
| `+` | ZkElement? | Add a ZkElement at the current building point. |
| `-` | ZkElement? | Remove a ZkElement from the children of current ZkElement. The element will be paused. |
| `+` | List&lt;ZkElement&gt; | Add all ZkElements in the list at the current building point. |
| `+=` | ZkElement, ZkElement? | Add the ZkElement to another. |
| `-=` | ZkElement | Remove a ZkElement from another.<br>Removes from `childElements` and from the DOM.<br>Pauses the child. |
| `-=` | KClass&lt;*&gt; | Remove all children that are instances of the given class. |

```kotlin
class OperatorExample(
    element: HTMLElement
) : ZkElement(element) {

    val container = ZkElement()

    override fun onCreate() {
        super.onCreate()

        + div(zkLayoutStyles.block) {
            + "text1"
            ! """<span style="font-size: 150%">text2</span>"""
            + (document.createElement("input") as HTMLElement) marginBottom 20

            + container

            container += buttonSuccess("A Button, click to hide the note") {
                container -= ZkNote::class
            } marginBottom 20

            container += noteSuccess("A Note", "This note will disappear shortly.")
        }
    }
}
```

The example above results in this:

<div data-zk-enrich="OperatorExample"></div>

### Special Inserts

Insert a child before the first one.

```kotlin
insertFirst(child)
```

Insert a child after another.

```kotlin
insertAfter(childToInsert, insertAfterThis)
```

Insert a child before another.

```kotlin
insertBefore(childToInsert, insertBeforeThis)
```

### Clear

Remove and destroy all children.

```kotlin
clearChildren()
```

Remove and destroy all children and clear the `element` DOM node.

```kotlin
clear()
```

### Get and Find

To find a child you can use the getter / finder functons. These functions find only direct children.

Get the first child of a given class. Throws `NoSuchElementException` if there is no such child.

```kotlin
this[ZkNote::class]
```

Get the first child which has "cssClassName" in the class list. Throws `NoSuchElementException` if there is no such
child.

```kotlin
this["cssClassName"]
```

Get all children that are ZkNote instances.

```kotlin
find<ZkNote>()
```

Get the first child for the given class. Throws `NoSuchElementException` if there is no such child.

```kotlin
findFirst<ZkNote>()
```

Check if there is at least one child of a given class.

```kotlin
hasChildOf<ZkNote>()
```

### Event Listeners

To listen on browser events use the `on` function. This function adds the event listener at the build point.

```kotlin
on("click") { println("clicked") }
```

It is possible to combine operators with `on`.

```kotlin
val child = ZkElement()
+ child.on("click")
```

To use the event object the browser supplies.

```kotlin
on("click") { event ->
  event as MouseEvent
  /* ... */
}
```

Note that in the case below the build point is `element` of `selectedOption`.

```kotlin
val selectedOption = ZkElement()
selectedOption.on("click") { toggleOptions() }
```

You can pass a function reference to `on`.

```kotlin
override fun onCreate() {
  on("click", ::onClick)
}

fun onClick(event: Event) {
  // ...
}
```

## Data load

To perform io operations, use the `io` block. This launches a coroutine. You can build the element as usual inside
the `io` block, but pay attention: this code will run after the data fetch is finished. Code placed after the `io` block
runs first.

```kotlin
io {
  ExampleDto.all().forEach { + it.name }
}

+ "Hello" // this is the first to run
```

### More than one fetch [source code](../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/ParallelDownloadExample.kt)

To perform more than one fetch use `coroutineScope`.

Remember that the order of launch block execution run **is random**.

It **is guaranteed** that all internal launch blocks finish before anything after
`coroutineScope` is executed.

This may have an effect on data fields and on the elements of the UI. For example:
`insertFirst` and `insertBefore` has to be correct, or the UI will be confused.

Example (source code link above):

<div data-zk-enrich="ParallelDownloadExample"></div>

```kotlin
override fun onCreate() {
  super.onCreate()

  io {
    coroutineScope {
      launch { fetched = serverMock(4, "first") }
      launch { serverMock(2, "second").forEach { + it } }
    }

    fetched.forEach { + it }

    - loading
  }

  + loading
}
```

## Timeline

* 2021.5.12
  * rename convenience functions from noteXX to XXnote

