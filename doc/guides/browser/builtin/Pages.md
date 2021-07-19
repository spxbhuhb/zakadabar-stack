# Pages

Pages are typically top level elements that can be use as routing targets. The stack provides a number of built-in page
classes to use or extend.

## ZkPage

This is the simplest page. You can use it to hold fixed or scrolling content. [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/pages/PageExample.kt)

```kotlin
class ExamplePage : ZkPage() {

    override fun onCreate() {
        + "The content of the page."
    }

}
```

<div data-zk-enrich="PageExample"></div>

If you check the actual source code of the example you will see an `appTitle = false` line. This is important when you
use a page inside another like we do with this example. When `appTitle` is true (the default), pages set the application
title automatically when their `onResume` function runs.

There are a few things to mention that is not trivial looking at the example.

When you use ZkPage without any constructor parameters it will use:

* the default layout (comes from [Routing](../structure/Routing.md),
* the [zkPageStyles.scrollable](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/zkPageStyles.kt) CSS class.

You can override both by passing the appropriate constructor parameter. The other built-in values defined are:

* [ZkFullScreenLayout](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/ZkFullScreenLayout.kt)
* [zkPageStyles.fixed](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/zkPageStyles.kt)



