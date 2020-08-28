# CSS

Working example and explanations: [Hello World](https://github.com/spxbhuhb/zakadabar-samples/tree/master/01-beginner/hello-world).

## Writing CSS

To add CSS to your module extend the [CssStyleSheet](../../../src/jsMain/kotlin/zakadabar/stack/frontend/util/css.kt)
class.

```kotlin
class HelloWorldClasses(theme: Theme) : CssStyleSheet<HelloWorldClasses>(theme) {

    companion object {
        val helloWorldClasses = HelloWorldClasses(FrontendContext.theme).attach()
    }

    val welcome by cssClass {
        display = "flex"
        flexDirection = "column"
        justifyContent = "flex-start"
        alignItems = "center"
        height = "100%"
        color = theme.darkestGray
    }
}
```

## Using CSS

Use the variable declared in the companion object of your CSS class:

```kotlin
element.className = helloWorldClasses.welcome
```