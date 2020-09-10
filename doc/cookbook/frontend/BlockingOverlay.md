# Blocking Overlay

**IMPORTANT** This page describes a feature request, this is not working yet.

Use this element to block user interactions on another element for a while.

**Highlights**

- [BlockingOverlay](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/util/BlockingOverlay.kt)
- Add to the element that is to be blocked.
- Remove from the element when blocking is no longer necessary.
- Blocks user interactions with a high z-index overlay.
- Size is the same as parent element size.
- Follows parent size changes with a [ResizeObserver](../../../src/jsMain/kotlin/zakadabar/stack/frontend/util/w3c/ResizeObserver.kt)
- Shows information/control for the user (may contain progress and/or cancel functionality).

## Add and Remove a Blocking Overlay

```kotlin
class Undercover : ComplexElement() {
 
    override fun init() : Undercover {
        this += BlockingOverlay(this, Icons.close.complex16(::closeOverlay))
    }

    private fun closeOverlay() {
        this -= BlockingOverlay::class
    }

}
```
