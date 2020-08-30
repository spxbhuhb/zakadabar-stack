# Espresso: Mind the Gap

I've been building a page, and I wanted a welcome message positioned, not at the top, but a bit down.
Easy - I thought - and naively added a `gap` to add some extra space between the content and the top of the page.

```kotlin
with (classes) {
    this@MyWelcome cssClass welcome build {
        + gap(height = "20%")
        + div(welcomeTitle) { + ta("welcome") }
    }
}
```

In Firefox this works well, but in Safari the gap disappears.

First, a word about why I want a gap in the first place. Why not a top margin or padding.
The reason is that both would be linked to an element which has nothing to do with the gap itself.

For example, this might work:

```kotlin
with (classes) {
    this@MyWelcome cssClass welcome build {
        + div(welcomeTitle) { 
            current.style.marginTop = "20px"
            + t("welcome")
        }
    }
}
```

However, let's assume I want something added to the top, then I have to move the marginTop again and again.
Because just adding something is probably wrong:

```kotlin
with (classes) {
    this@MyWelcome cssClass welcome build {
        
        + t("i want this first")

        + div(welcomeTitle) { 
            current.style.marginTop = "20px"
            + t("welcome")
        }
    }
}
```

So, I want my gap, but where is my gap in Safari?

Interestingly it is in the height of the parent DIV. If the parent DIV is not sized by me Safari will
use ... something, I don't exactly know what, but I guess it's close to zero (or the height of content maybe).

I've changed the minHeight of the parent DIV and everything worked.

```kotlin
val welcome by cssClass {
    display = "flex"
    flexDirection = "column"
    justifyContent = "flex-start"
    alignItems = "center"
    minHeight = "100%"
    width = "100%"
    color = theme.darkestGray
}
```

So, the right way to do this is to size the parent container properly. Actually it is logical in hindsight. :)