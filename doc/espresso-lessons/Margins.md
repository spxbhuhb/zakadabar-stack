# When Margins Just Disappear

When you build a view you typically want to have leave some space
between elements, so you will use `marginBottom` and `marginLeft`
of the builder. 

That works well, but sometimes your margins disappear without trace.

Consider this code. When the event listener removes the first child
and adds the second, the margin disappears.

```kotlin
override fun init(): ComplexElement {
    build(coreClasses.contentColumn) {
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
    build(coreClasses.contentColumn) {
        +complex {
            + child1
        }.marginBottom(20)
    }

    return this
}

private fun onUserClicked(event: Event) {
    this -= child1
    this += child2
}
```

