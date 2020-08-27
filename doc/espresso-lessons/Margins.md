# Espresso: Margins

## Oh, they collapse

Margins tend to collapse. Sometimes I remember. Sometimes I don't and when this happens I usually get
confused why my page doesn't show properly. 

I've read up on them, and I've found an advice: use only bottom margins.

This is actually a very good advice, and I started to follow it. This is why the leftMargin and topMargin
functions are missing from the DOMBuilder. You could add them by yourself of course, we are talking
about Kotlin here, but it is better not to use them in my opinion.

You can read more about this topic on these pages:

* [Single-direction margin declarations](https://csswizardry.com/2012/06/single-direction-margin-declarations/)
* [Everything You Need To Know About CSS Margins](https://www.smashingmagazine.com/2019/07/margins-in-css/)

## When They Just Disappear

When you build a view you typically want to have leave some space
between elements, so you will use `marginBottom` and `marginLeft`
of the builder. 

That works well, but sometimes your margins disappear without a trace.

Consider this code. When the event listener removes the first child
and adds the second, the margin disappears.

```kotlin
override fun init(): ComplexElement {
    this cssClasses coreClasses.contentColumn build {
        + child1.marginBottom(20)
    }

    return this
}

private fun onUserClicked(event: Event) {
    this -= child1
    this += child2
}
```

The solution would be to add the margin to the component itself or -
for more complex builds - add it to the end of a block like this:

```kotlin
override fun init(): ComplexElement {
    this cssClasses coreClasses.contentColumn build {
        +complex() build {
            + child1
        } marginBottom 20
    }

    return this
}

private fun onUserClicked(event: Event) {
    this -= child1
    this += child2
}
```

