# CSS

Example: [My Life, My Theme, My Style](https://github.com/spxbhuhb/zakadabar-samples/tree/master/01-beginner/my-life-my-theme-my-style).

## Write CSS

To add CSS to your module extend the [CssStyleSheet](../../../src/jsMain/kotlin/zakadabar/stack/frontend/util/css.kt)
class.

```kotlin
/**
 * This is a CSS style sheet. From other pieces of yhe code you can use it by the
 * [sampleClasses] field of the companion object.
 *
 * There is some magic involved here, but you don't have to worry about it. At
 * the end you just use the fields defined here.
 *
 * The important thing is that in the Stack CSS classes are plain old Kotlin classes,
 * you can do whatever you want to do with them.
 *
 * In general we expect to have fields of String type with a value that is known
 * as a CSS class by the browser.
 */
class SampleClasses(theme: Theme) : CssStyleSheet<SampleClasses>(theme) {

    companion object {
        /**
         * We will use this field to access the CSS style sheet. Defining it as
         * `val` means that it cannot be overridden, code in this module will
         * use exactly this style sheet. You can define it as `var` and then
         * it may be changed.
         *
         * IMPORTANT ---- Note the attach() at the end! ----
         *
         * That [CssStyleSheet.attach] call adds the style sheet to the browser. There is also
         * a [CssStyleSheet.detach] function that removes the style sheet.
         *
         * This particular call uses the default theme from the [FrontendContext].
         */
        val sampleClasses = SampleClasses(FrontendContext.theme).attach()
    }

    val aRealOne by cssClass {
        marginLeft = 100
        marginTop = 100
        width = "50%" // Some values accept Any. Int will be converted into "px", others will be uses as String
        height = 600 // This will be "600px"
        maxHeight = "600px !important" // You can add important if you wish
        border = "1px solid red"
    }

    val box1 by cssClass {
        margin = 4
        padding = 4
        border = "1px solid green"
    }

    val box2 by cssClass {
        display = "flex"
        flexDirection = "column"
        justifyContent = "flex-start" // If you hover your mouse above justifyContent you'll get help.
        alignItems = "center" // In case you always forget which is which. :D
        backgroundColor = theme.lightColor // Use a color from the theme.
        margin = 4
        padding = 4
    }

    val bigBoldWolf by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        fontWeight = 700
    }

    // let's play a bit around

    val sillyColor by cssClass {

        val v = fourRandomInt().map { min(it, 255) }

        color = "rgba(${v[0]},${v[1]},${v[2]},1)"

    }

}
```

## Use CSS

```kotlin
class StyleSamples : ComplexElement() {

    override fun init(): ComplexElement {

        val classes = sampleClasses

        // You can use the "className" and "classList" properties of
        // Element, HTMLElement, SimpleElement, ComplexElement.

        className = "my-very-own-and-very-non-existing-class"
        classList += "yet-another-imaginary-class-now-we-have-two"
        classList -= "this-i-really-want-to-remove"

        this cssClass classes.aRealOne build {

            // The "cssClass" function works on Elements, HTMLElements, SimpleElements
            // and ComplexElements, it is quite handy.

            + div() cssClass classes.box1 build {
                + "this is displayed in a random color: "
                // I use "!" here because otherwise the two strings are added together :)
                ! "blah blah blah" cssClass classes.sillyColor
            }

            // You can use "with" to avoid typing "classes." again and again.
            // If you use "with" read up on it, it's tricky, it can hide members
            // of your class if there are name collisions.

            with(sampleClasses) {

                // You can use the method call if you prefer, it is the same
                // with Kotlin infix functions. I like to avoid all the dots
                // and parenthesis tho as without them the code is much more
                // readable.

                + div().cssClass(box2) build {

                    + "this is big and bold" cssClass classes.bigBoldWolf

                }

            }

        }

        return this
    }

}
```

## Extend CSS

To extend an existing CSS class simply extend it as any other Kotlin class.

**Important** Pay attention to the cast in the companion object!

```kotlin
open class DesktopClasses(theme: Theme) : HeaderClasses(theme) {

    companion object {
        val desktopClasses = DesktopClasses(theme).attach() as DesktopClasses
    }

    val desktop by cssClass {
        boxSizing = "border-box"

        width = "100%"
        height = "100%"

        display = "flex"
        flexDirection = "column"

        borderRadius = 2
    }
}
```