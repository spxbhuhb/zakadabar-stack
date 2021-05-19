# Themes, CSS

The stack contains a dynamic theme and style system.

* [theme](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/theme.kt)
    * stores the active theme
    * when changes, all styles are rebuilt
* [ZkTheme](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/theme.kt)
    * interface for themes to implement
* [ZkCssStyleSheet](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/css/ZkCssStyleSheet.kt)
    * CSS style sheet builder / manager
* [ZkCssStyleRule](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/css/ZkCssStyleRule.kt)
    * one CSS rule (with possible variations)
* [ZkColors](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/ZkColors.kt)
    * Color constants, palettes from Material Colors and the Zakadabar palette
* [zkHtmlStyles](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/theme/zkHtmlStyles.kt)
    * global HTML styles (on "body", "a" etc. tags)

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

[initTheme](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/theme.kt),
called from `jsMain/main.kt`, selects the theme during application startup.

Algorithm:

1. check account theme, if present, find the theme with the given name
   in `application.themes`
2. check local storage for `zk-theme-name`, if present, find the theme with the
   given name in `application.themes`
3. first theme in `application.themes`, if not empty
4. ZkBuiltinLightTheme

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Add Themes In main.kt">
The initial theme selection works only if you add the available themes to application.themes
in main.kt as the example shows below.
</div>

The order of calls is important here. `sessionManager.init()` downloads the
account settings which are used during theme selection. Also you should add all
themes before calling `initTheme`.

```kotlin
with(application) {

    initSession()

    initTheme(SiteDarkTheme(), SiteLightTheme())

    // ...other steps here...
}
```

### Write a theme

* Extend one of the built-in themes or
  implement [ZkTheme](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/theme.kt)
  .
* Use `onResume` to fine tune style variables.
* Calling `super.onResume` is not mandatory. It applies the style modifications of the
  super class.

```kotlin
class ExampleThemeGreen : ZkBuiltinLightTheme() {

    override var name = "example-green"

    override var primaryColor = ZkColors.Green.c500

    override fun onResume() {
        super.onResume()  // apply defaults from built-in light theme, not mandatory

        with(zkTitleBarStyles) {
            appHandleBackground = ZkColors.Green.c500
            titleBarBackground = ZkColors.Green.c500
        }

    }
}
```

### Change The Theme [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/theme/ThemeExample.kt)

Assign a theme instance to `theme` to set the active theme.

```kotlin
theme = ZkBuildinLightTheme()
```

These two are somewhat simple examples, for example they don't set markdown colors properly:

<div data-zk-enrich="ThemeExample"></div>

This is a fine-tuned example, that can be used to replace one of the default themes in production:

<div data-zk-enrich="ThemeShowCase"></div>

## CSS

* CSS style sheets are written in Kotlin.
* Extend [ZkCssStyleSheet](/src/jsMain/kotlin/zakadabar/stack/frontend/resources/css/ZkCssStyleSheet.kt) to write a new one.
* Use `val yourStyles by cssStyleSheet(YourStyleSheet())` to make an instance of the style sheet.
* When the theme changes, all style sheets are recompiled.
    * During recompile the style names remain the same.
    * All style sheet parameters are reset before call of `theme.onResume`. 
    * Variables of the style sheets may be set by `theme.onResume`.

### Write a CSS Style Sheet

The following example shows a simple style sheet. Note the use of `cssParameter`.
This is the proper way to define parameters that you offer for the themes to
modify.

The important thing about `cssParameter` is that the initialization function
**runs at every theme change**, before `theme.onResume`. This guarantees that
every `onResume` starts with a clean parameter set, no leftovers from the 
previous theme are there.

If you define a parameter which has the same name as a css rule name, you have
to pay attention to the assignment. In the example `backgroundColor` shows
how to do this properly.

```kotlin
val exampleStyles by cssStyleSheet(ExampleStyles())

open class ExampleStyles : ZkCssStyleSheet() {

    open var myStyleParameter by cssParameter { 10 }
    open var backgroundColor by cssParameter { theme.dangerColor }

    open val exampleStyle by cssClass {
        height = myStyleParameter
        width = 20
        backgroundColor = this@ExampleStyles.backgroundColor
    }

}
```

### Use a CSS Class

Use operators to add and remove CSS classes:

```kotlin
+ div {
    + exampleStyles.exampleStyle // adds the class to classList
    - exampleStyles.exampleStyle // removes the class from classList
    ! exampleStyles.exampleStyle // removes all other classes and adds this one
}
```

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

`className` is the standard HTML `className` attribute, a String.

```kotlin
+ zke {
    className = exampleStyles.exampleStyle.cssClassname
}
```

`classList` is the standard HTML `classList` attribute, a `DOMTokenList`.

```kotlin
+ zke {
    classList += exampleStyles.exampleStyle
    classList -= exampleStyles.exampleStyle
    classList += exampleStyles.exampleStyle
}
```

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Context Dependent">
All operators, className and classList is context dependent. Pay attention where you use them.
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

```
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

You can add a media query to a CSS class by using the `media` parameter of
the `on`
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

CSS classes defined with `cssClass` have automatically generated names to avoid
collisions.

To use a fix name pass it as parameter to `cssClass`

```kotlin
val myClass by cssClass("fixed-class-name") { }
```

### Selector Only Rules

The function `cssRule` let you specify whatever selector you would like instead
of a class name.

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

You can combine your classes with selectors. The example below will set the
styles for `<a>` tags under the element that has the `content` css class. Note that the
actual CSS class name of `content` is automatically generated.

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

This import is dynamic, if you store the URL in the theme the import can be
theme-aware.

For example `Lib:Markdown` uses `highlight.js` for code syntax highlight. The
colors have to be different in dark and light mode. `highlight.js` does not
support multiple themes,
so [MarkdownStyles](../../../../../lib/markdown/src/jsMain/kotlin/zakadabar/lib/markdown/frontend/markdownStyles.kt)
use a variable and the themes can set whatever URL they want.

```kotlin
open var highlightUrl =
    "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.2/styles/idea.min.css"

val highlightStyles by cssImport {
    url = highlightUrl
}
```

### Replace CSS Style Sheets

<div data-zk-enrich="Note" data-zk-flavour="Danger" data-zk-title="Class Names">
Style switch after application.init is broken at the moment because it
does not carry the original class names. Fix is already planned for this, but it
is low priority.
</div>

Assign a different instance to the given style variable:

```kotlin
zkButtonStyles = MyButtonStyles()
```

## Inline Styles

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Inline Styles">

Use inline styles only when the component you use them on is a singleton, and
the given style is not used anywhere else. Of those conditions are not true, use
a style sheet.

</div>

### Shorthands

ZkElement provides a number of shorthand variables to set inline styles:

```kotlin
height = 400 // will be "400px"
height = "1em"
width = 400 // will be "400px"
width = "1em"
gridTemplateRows = "100px max-content"
gridTemplateColumns = "100px max-content"
gridGap = theme.spacingStep
```

### The styles Function

Add inline styles to any element (Zk or HTML) with the `styles` function. This
function executes a builder
on [CSSStyleDeclaration](https://developer.mozilla.org/en-US/docs/Web/API/CSSStyleDeclaration)
, which is a standard Web API.

```kotlin
+ div {
    styles {
        height = "400px"
        setProperty(
            "grid-template-rows",
            "100px 100px"
        ) // for names not defined
    }
}
```

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Strings">

`CSSStyleDeclaration` properties are strings. Therefore, you cannot use integers
as in `cssClass`.

</div>

## Scroll Bar Styles

Scroll bars are styled
by [ZkScrollBarStyles](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/zkScrollBarStyles.kt)
.

The default is to use scroll bar colors aligned with the theme.

To switch off scroll bar set `scrollBarStyles.enabled` to `false`.

```kotlin
scrollBarStyles.enabled = false
```

## Timeline

### Changes

* 2021.5.18
    * introduce `cssParameter`, changes applied in `theme.onResume` are independent
* 2021.5.16
    * change delegated property type from string to ZkCssStyleRule
    * operators to add/remove rules easily