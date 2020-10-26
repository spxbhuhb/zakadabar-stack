This example shows the ways to build the HTML DOM inside the browser using
the DOMBuilder, SimpleElement and ComplexElement.

**Shortcuts**

* [HtmlSamples.kt](src/jsMain/kotlin/zakadabar/samples/waystohtml/frontend/HtmlSamples.kt)
* [main.kt](src/jsMain/kotlin/main.kt)

**Important points**

This example uses margins quite often. In general that is not a good practice, it would be
better to have CSS styles. Check [Espresso: Margins](https://github.com/spxbhuhb/zakadabar-stack/doc/espresso-lessons/Margins.md)
for thought about margins.

**Running the example**

This example does not need a backend. To run it, execute this Gradle task. It won't finish,
it just keeps running until you stop it manually.

* 01-beginner / 5-ways-to-html / Tasks / kotlin browser / jsBrowserRun

When it is up and running a web browser opens with the URL: http://localhost:3000

This is where you can see what happens in HtmlSamples application.

You can make the WebPack server refreshing the page automatically by adding "--continuous"
to the program arguments of the IDEA Run Configuration.

**Don't forget to stop other examples before you start a next one.**