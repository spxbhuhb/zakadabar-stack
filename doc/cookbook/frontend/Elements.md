# Elements

## Simple Elements

Working example:
- [Initials.kt](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/util/Initials.kt)
- [EntityStatusCard.kt](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/util/EntityStatusCard.kt)

Use simple elements when there is nothing special to do but to set the content.

```kotlin
class Initials(displayName: String) : SimpleElement() {

    override fun init() : Initials {

        innerText = if (displayName.length < 2) {
            displayName.toUpperCase()
        } else {
            val e = displayName.split(" ")
            if (e.size < 2) {
                displayName.substring(0, 2).toUpperCase()
            } else {
                "${e[0][0].toUpperCase()}${e[1][0].toUpperCase()}"
            }
        }

        className = coreClasses.avatar

        return this
    }

}
```

## Complex Elements

Working example:
- [EntityStatusCard.kt](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/util/EntityStatusCard.kt)

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
class Example : ComplexElement() {
    override fun init(): SimpleElement {
        on("click") { println("click") }
    }
}
```

## Build Structures

Working examples:
- [RealSimple.kt](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/witches-brew/src/jsMain/kotlin/zakadabar/samples/wichesbrew/frontend/RealSimple.kt)
- [DeepInTheForest.kt](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/witches-brew/src/jsMain/kotlin/zakadabar/samples/wichesbrew/frontend/DeepInTheForest.kt)
- [HtmlSamples.kt](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/5-ways-to-html/src/jsMain/kotlin/zakadabar/samples/waystohtml/frontend/HtmlSamples.kt)

It is very common, that a frontend element has an internal layout with a lot of "div"-s, other elements, etc.
To support this use case you can use the [DOMBuilder](../../../src/jsMain/kotlin/zakadabar/stack/frontend/elements/DOMBuilder.kt).

Important points:

* "+" on String adds text safely, no need for escape.
* "!" sets innerHTML and **must** be properly escaped.
* you can use "+" on a HTMLElement without restriction
* you can use "+" on a SimpleElement without restriction
* you can use "+" on a ComplexElement only from builders that are run in a ComplexElement

```kotlin
class RealSimple : SimpleElement() {

    private val css = cauldronClasses

    override fun init(): SimpleElement {

        className = css.realSimple

        build {
            + div(css.text1) { +t("text1") }
            + div(css.text2) { !"""<span style="font-size: 150%">↫</span> ${t("text2")}""" }
        }

        return super.init()
    }
}
```

When you add ComplexElement instances they will be children of the ComplexElement that performs the building.
The DOM and the ComplexElement structure will be different in this case, so if you want to swap elements dynamically
you have to use a wrapper:

```kotlin
class DeepInTheForest : ComplexElement() {

    override fun init(): ComplexElement {

        val sometimesThis = SimpleText(" is this the shortest route")
        val sometimesThat = SimpleText(" are we lost")

        val wrapper = ComplexElement()

        build {
            + div {
                + row {
                    ! "we are deep in the forest ...&nbsp;"
                    + wrapper
                    + " ?"
                }
            }
        }
    
        var round = 0

        launch {
            while (true) {
                delay(2000)
                wrapper -= SimpleText::class
                wrapper += if (round % 2 == 0) sometimesThat else sometimesThis
                round ++
            }
        }

        return this
    }
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