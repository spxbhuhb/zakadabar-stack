# CSS and Theming

The stack contains a dynamic theme and style system. You can override all colors and styles.

[ZkApplication](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt)

* `theme` property stores the active theme
  * when changes, all styles are rebuilt
  * `themes` property stores the themes known to the application
* [ZkTheme](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/ZkTheme.kt)
  * interface for themes to implement
* [ZkCssStyleSheet](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/css/ZkCssStyleSheet.kt)
  * CSS style sheet builder / manager
* [ZkColors](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/ZkColors.kt)
  * Color constants, palettes from Material Colors and the Zakadabar palette

## Theme

* Themes define values used by style sheets.
* Themes may fine-tune style sheets in their `onResume` method.
* Each theme has a `name` which identifies the theme.
* Each style sheet can access the current theme in their `theme` property
* Changing the theme:
  * rebuilds all attached style sheets.
  * **does not** change the DOM (except the `zk-styles` node under `body`)
* Built-in themes:
  * [ZkBuiltinLightTheme](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/theme/ZkBuiltinLightTheme.kt)
    - light theme
  * [ZkBuiltinDarkTheme](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/theme/ZkBuiltinDarkTheme.kt)
    - dark theme

### Initial Theme Selection

`ZkApplication.initTheme`, called from `jsMain/main.kt`, selects the theme during application startup.

Algorithm:

1. check account theme, if present, find the theme with the given name in `ZkApplication.themes`
2. check local storage for `zk-theme-name`, if present, find the theme with the given name in `ZkApplication.themes`
3. first theme in `ZkApplication.themes`, if not empty
4. ZkBuiltinLightTheme

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Add Themes In main.kt">
The initial theme selection works only if you add the available themes to ZkApplication.themes
in main.kt as the example shows below.
</div>

The order of calls is important here. `sessionManager.init()` downloads the account settings which are used during theme
selection. Also you should add all themes before calling `initTheme`.

```kotlin
with(ZkApplication) {

  sessionManager.init()

  themes += SiteDarkTheme()
  themes += SiteLightTheme()

  theme = initTheme()

  // ...other steps here...
}
```

### Write a theme

* Extend one of the built-in themes or
  implement [ZkTheme](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/ZkTheme.kt).
* Use `onResume` to fine tune style variables.
* If you set a style variable, **all of your themes has to set that particular variable**, see warning in Change The
  Theme.

```kotlin
class ExampleThemeGreen : ZkBuiltinLightTheme() {

  override var name = "example-green"

  override var primaryColor = ZkColors.Green.c500

  override fun onResume() {
    super.onResume()  // apply defaults from built-in light theme

    with(zkTitleBarStyles) {
      appHandleBackground = ZkColors.Green.c500
      titleBarBackground = ZkColors.Green.c500
    }

  }
}
```

### Change The Theme [source code](../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/theme/ThemeExample.kt)

Assign a theme instance to `ZkApplication.theme` to set the active theme. Be careful, your theme system should be
consistent (see warnings above and below)!

```kotlin
ZkApplication.theme = ZkBuildinLightTheme()
```

<div data-zk-enrich="Note" data-zk-flavour="Danger" data-zk-title="Theme Changes Are Cumulative">
As of now theme changes are cumulative. You have to set all changed values in onResume overrides
properly. The example below shows what happens when you don't do that: the standard built-in
themes do not reset values that are changed by the example themes (these ones also miss markdown
styles, so the page layout brakes down).
</div>

<div data-zk-enrich="ThemeExample"></div>

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Page Refresh">
If you clicked on "Green" or "Red" above, you have to refresh the page to set your colors right.
Sorry about the mess, told you that changes are cumulative. :P
</div>

## CSS

* CSS style sheets are written in Kotlin.
* Extend [ZkCssStyleSheet](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/css/ZkCssStyleSheet.kt) to write a new
  one.
* Use `val yourStyles by cssStyleSheet(YourStyleSheet())` to make an instance of the style sheet.
* When the theme changes, all style sheets are recompiled.
* During recompile the style names remain the same.
* Variables of the style sheets may be set by `onResume` of the theme.

### Write a CSS Style Sheet

```kotlin
val exampleStyles by cssStyleSheet(ExampleStyles())

open class ExampleStyles : ZkCssStyleSheet<ZkTheme>() {

  open var myStyleParameter: Int = 10

  open val exampleStyle by cssClass {
    height = myStyleParameter
    width = 20
    backgroundColor = ZkColors.Green.c500
  }

}
```

### Use a CSS Class

Pass the class name to the helper methods in ZkElement:

```kotlin
+ div(exampleStyles.exampleStyle) {
  + "Styled content"
}
```

Use the `css` inline function:

```kotlin
+ ZkElement() css exampleStyles.exampleStyle
```

Use `className` set the classes, this removes all classes set before:

```kotlin
+ zke {
  className = exampleStyles.exampleStyle
}
```

Use `classList` to manipulate the class list

```kotlin
+ zke {
    classList += exampleStyles.exampleStyle
    classList -= exampleStyles.exampleStyle
    classList += exampleStyles.exampleStyle
}
```

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Context Dependent">
className and classList is context dependent. Pay attention where you use them.
</div>

### CSS Values

Many CSS keywords have convenient setter method you can use inside `cssClass`.

If you need one that does not have a setter, use the `styles` property:

```kotlin
val myClass by cssClass {
    styles["-webkit-transform"] = "scale(0.2)"
}
```

### Class And Selectors

You can use the `on` method to define styles with additional selectors:

```kotlin
open val exampleStyle by cssClass {
    color = ZkColors.white

    on(" a") {
        color = ZkColors.black
    }

    on(":active") {
        color = ZkColors.black
    }
}
```

In compiled CSS this looks like:

```css
.ExampleStyles-exampleStyle-123 {
    color: #ffffff;
}

.ExampleStyles-exampleStyle-123 a {
    color: #000000;
}

.ExampleStyles-exampleStyle-123:active {
    color: #000000;
}
```

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Space Before">
Note the space before the "a". It is important as the stack does not add anything
between the style name and the parameter of "on".
</div>

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="zk-styles">
If you open your browser inspector, there is a div with id "zk-styles" under
the body. This contains all styles compiled by the stack.
</div>

### Media Queries

You can add a media query to a CSS class by using the `media` parameter of the `on`
method:

```kotlin
val myClass by cssClass {
    on(media = "(max-width: 600px)") {
        // ...
    }
}
```

### Shorthands

There are some shorthand selectors defined for convenience. Use them like this:

```kotlin
val myClass by cssClass {
    hover { /* ... */ }
}
```

| Shorthand | CSS |
| --- | --- |
| `hover` | `:hover` |
| `small` | `@media(max-width: 600px)` |
| `medium` | `@media(max-width: 800px)` |
| `large` | `@media(min-width: 800px)` |

### Fixed Class Name

CSS classes defined with `cssClass` have automatically generated names to avoid collisions.

To use a fix name pass it as parameter to `cssClass`

```kotlin
val myClass by cssClass("fixed-class-name") { }
```

### Selector Only Rules

The function `cssRule` let you specify whatever selector you would like instead of a class name.

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Space Before">
At the moment the "on" method and the shorthands <b>do not work</b> inside cssRule.
</div>

In this case the property name ("link" in this case) is mostly useless.

```kotlin
val link by cssRule("a") {
    color = theme.color.info
    textDecoration = "none"
}
```

You can combine your classes with selectors. The example below will set the styles for
`<a>` tags under the element that has the `content` css class. Note that the actual CSS class name of `content` is
automatically generated.

```kotlin
val content by cssClass {

}

val link by cssRule(".$content a") {
    color = theme.color.info
    textDecoration = "none"
}
```

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Accessing non-final property content in constructor">
You may get this warning if you use the combination like above and your variable is declared "open". Fix this
by setting the variable final.
</div>

### Import

Use `cssImport` to import a style sheet.

```kotlin
val imported by cssImport {
    url = "imported.css"
}
```

This import is dynamic, if you store the URL in the theme the import can be theme-aware.

For example `Lib:Markdown` uses `highlight.js` for code syntax highlight. The colors have to be different in dark and
light mode. `highlight.js` does not support multiple themes,
so [MarkdownStyles](../../../../lib/markdown/src/jsMain/kotlin/zakadabar/lib/markdown/frontend/MarkdownStyles.kt)
use a variable and the themes can set whatever URL they want.

```kotlin
open var highlightUrl = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/idea.min.css"

val highlightStyles by cssImport {
    url = highlightUrl
}
```

### Replace CSS Style Sheets

<div data-zk-enrich="Note" data-zk-flavour="Danger" data-zk-title="Class Names">
Style switch after ZkApplication.init is broken at the moment because it
does not carry the original class names. Fix is already planned for this, but it
is low priority.
</div>

Assign a different instance to the given style variable:

```kotlin
zkButtonStyles = MyButtonStyles()
```

## Scroll Bar Styles

Scroll bars are styled
by [ZkScrollBarStyles](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/zkScrollBarStyles.kt)
.

The default is to use scroll bar colors aligned with the theme.

To switch off scroll bar set `scrollBarStyles.enabled` to `false`.

```kotlin
scrollBarStyles.enabled = false
```