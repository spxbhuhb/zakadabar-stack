# Elements

## Simple Elements

Use simple elements when there is nothing special to do:

```kotlin
class Initials(displayName: String) : SimpleElement() {

    init {
        innerText = if (displayName.length < 2) {
            displayName
        } else {
            val e = displayName.split(" ")
            if (e.size < 2) {
                displayName.substring(0, 2)
            } else {
                "${e[0][0].toUpperCase()}${e[1][0].toUpperCase()}"
            }
        }

        className = coreClasses.avatar
    }

}
```

## Complex Elements

Use complex elements: 

* to build changing element trees
* for event listeners
* for message handlers
* for dynamic add, remove, replace

Complex elements perform proper cleanup when they are removed.

## Listen to Browser Events

* you need ComplexElement for this
* you may add event listeners using the `on` method
* ComplexElement.cleanup removes all listeners added with `on`

```kotlin
class Example() : ComplexElement() {
    override fun init(): SimpleElement {
        on("click") { println("click")}
    }
}
```

## Build Structures

It is very common, that a frontend element has an internal layout with a lot of "div"-s.
To support this use case you can use the DOM builder of SimpleElement.

Important points:

* "+" on String adds text safely, no need for escape.
* "!" sets innerHTML and **must** be properly escaped.
* you can use "+" on a SimpleElement without restriction
* you can use "+" on a ComplexElement only from builders that are run in a ComplexElement

```kotlin
override fun init(): SimpleElement {

    val css = helloWorldClasses

    build(css.welcome) {
        + image("jungle-sea.jpeg", css.welcomeImage)
        + div(css.welcomeTitle) { +thw("welcome") }
        + div(css.welcomeInstructions) { !"""<span style="font-size: 150%">↫</span> ${thw("click.on.something")}""" }
        + div(css.welcomeInstructions) { !"""<span style="font-size: 150%">↫</span> ${thw("dbclick.on.something")}""" }
    }

    return super.init()
}
```

```kotlin
override fun init(): SimpleElement {



    build {
        + prefixIcon
        + input
        + buttons.build(classes.inputPostfix) {
            + approveIcon.withClass(classes.inputPostfixIcon, classes.approveFill)
            + cancelIcon.withClass(classes.inputPostfixIcon, classes.cancelFill)
        }
    }

    return super.init()
}
```

When you add ComplexElement instances they will be children of the ComplexElement that performs the building.
The DOM and the ComplexElement structure will be different in this case, so if you want to swap elements dynamically
you have to use a wrapper:

```kotlin
val sometimesThis = StaticText("but this is the shortest route")
val sometimesThat = StaticText("and we are lost")

val wrapper = ComplexElement()
wrapper += sometimesThis

build {
    + div {
        + div {
            + "we are deep in the forest"
            + wrapper
        }
    }
}

launch {
    delay(1000)
    wrapper -= sometimesThis
    wrapper += sometimesThat
}
```

## Data load

### More than one fetch during init

To fetch more than one data set during the initialization of a ComplexElement
use `coroutineScope`.

Remember that the order in which the internal launch blocks run **is random**.

It **is guaranteed** that all internal launch blocks finish before anything after
`coroutineScope` is executed.

This my have an effect on data fields and on the elements of the UI. For example:
`insertFirst` and `insertBefore` has to be correct, or the UI will be confused.
 
```kotlin
class ParallelDownloadView(private val stuffId : Long) : ComplexElement() { 

    private val childNames = ComplexElement()
    private val statistics = ComplexElement()

    private lateinit var stuff : StuffDto
    private lateinit var children : List<StuffDto>

    override fun init(): SimpleElement {

        val loading = StaticText(t("loading"))

        this += loading // add loading, use variable so we can easily remove it later

        launch {        

            coroutineScope {
                launch {
                    stuff = StuffDto.comm.get(stuffId) // download the data from the server
                    insertFirst(StaticText(stuff.name)) // you can create elements on the fly if you don't want to access them easily later
                }
                launch {
                    children = StuffDto.comm.getChildren(stuffId) // download the children of stuff
                    childNames += children.map { StaticText(it.name) } // add a StaticText for each children with the name as content
                    insertBefore(children, loading) // insert the children before loading
                }
            }

            this -= loading // remove loading, we have all the data, no need for it any more

            statistics.innerHTML = "#${stuffDto.id}: ${stuffDto.size} bytes, ${children.size} children" // set statistics
            this += statistics // show it to the user
        }

        return super.init()
    }

}
```