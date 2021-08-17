# Icons

* Use [ZkIcon](/core/core/src/jsMain/kotlin/zakadabar/core/browser/icon/ZkIcon.kt) to display a simple icon.
* Get built-in icons from [ZkIcons](/core/core/src/jsMain/kotlin/zakadabar/stack/frontend/resources/ZkIcons.kt).
* Use a [button](./Buttons.md) instead to display an icon with containing box, fill, etc.
* Extend [ZkIconStyles](/core/core/src/jsMain/kotlin/zakadabar/core/browser/button/zkButtonStyles.kt) to override
  default styles and dimensions.

## Use an Icon

```kotlin
+ ZkIcon(ZkIcons.deleteForever)
```

Constructor parameters of [ZkIcon](/core/core/src/jsMain/kotlin/zakadabar/core/browser/icon/ZkIcon.kt):

| Parameter | Description |
| ---- | ---- |
| iconSvg | SVG source text of the icon. |
| iconSize | Desired size of the icon in pixels. |
| cssClass | Additional CSS class to add to the icon. |
| onClick | Function to call when the icon is clicked. |

## Add a New Icon

This process is for Material Icons, but it is easy to adjust to anything SVG.

1. Go to [Material Icons](https://material.io/resources/icons).
1. Select an icon.
1. Settings: Web, Density 1x, Size 24dp
1. Use "SVG" (at the bottom) to download the SVG file.
1. Open the downloaded file in a text editor.
1. Copy the part **BETWEEN** the "svg" tags to clipboard.
1. Use the `iconSource` function to create a new icon with the copied SVG.

```kotlin
val arrowRight by iconSource("""<path d="M10 17l5-5-5-5v10z"/><path d="M0 24V0h24v24H0z" fill="none"/>""")
```

### Reasons Behind the Way of Add

There are a few reasons why the process above is somewhat complex:

* Color the icons with CSS `fill`.
    * This doesn't work if the SVG contains the color, which the downloaded file does.
    * Browsers are a bit hectic with in-line SVG, this way the `svg` tag is added the proper way.
* Resizing SVG icons needs the attributes of the `svg` tag set.
    * This is pretty easy when our code creates the tag. Modification an existing tag is much more complex.
* Avoid icon fonts, we've had many troubles with the swapping, download etc.
* Author of Glyphicons suggested SVG icons for many reasons.

# Built-In Icons [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/icon/IconExamples.kt)

<div data-zk-enrich="IconExamples"></div>