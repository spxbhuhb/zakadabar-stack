# Icons

## Icons Included in the Stack

The stack uses SVG icons. You may find the included ones in: [Icons](../../src/jsMain/kotlin/zakadabar/stack/frontend/components/Icons.kt)

To use with a pre-defined size (16, 18, 20), simply add them to your element:

```kotlin
this += Icons.cancel.simple16
```

```kotlin
this += Icons.questionMark.simple16.withClass(headerClasses.extensionIcon16)
```

Icons come in two favors: simple and complex. Simple ones extend SimpleElement, complex ones extend ComplexElement.

If you want to add an action to call when the user clicks on the icon use a complex icon. (You cannot add actions to 
simple ones with the `on` method.)

```kotlin
this += Icons.edit.complex16 { println("click") }
```

```kotlin
private fun onCancel() {
    println("cancel")
}

this += Icons.cancel.complex16(::onClick)
```

## Global Navigation on Click

To replace the main instance shown by the desktop:

```kotlin
this += Icons.edit.complex16 { dispatcher.postSync { GlobalNavigationRequest("/url/to/go") } }
```

## Switch Views in Place

To replace views in-place use a [SwitchView](../../src/jsMain/kotlin/zakadabar/stack/frontend/components/composite/SwitchView.kt)

For recipes about switching views, check [Switching Views](SwitchViews.md)

```kotlin
this += Icons.edit.complex16 { switchView.next() }
```
