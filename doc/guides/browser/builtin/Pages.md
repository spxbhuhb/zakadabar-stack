# Pages

Pages are typically top level elements that can be use as routing targets. The stack provides a number of built-in page
classes to use or extend.

## State and Routing

As pages can be routing targets, they have a `route` method that is called whenever
the navigation state of the application (the URL) changes (and it points to this page).

The `route` method supports two use patterns:

- create a new instance at each navigation change
- use the same instance all the time

The property `factory` chooses between this behaviour:

- when true, each navigation change creates a new instance
- when false, the same instance is used (the actual routing target)

Default is **true**, meaning that a new instance is created at each
navigation change.

To change this behaviour:

```kotlin
class MyPage : ZkPage() {
    override val factory = false
}
```

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
* the [zkPageStyles.scrollable](/core/core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/zkPageStyles.kt) CSS class.

You can override both by passing the appropriate constructor parameter. The other built-in values defined are:

* [ZkFullScreenLayout](/core/core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/ZkFullScreenLayout.kt)
* [zkPageStyles.fixed](/core/core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/zkPageStyles.kt)

## ZkArgPage

`ZkArgPage` is a base class for pages that receive parameters in the URL, such as queries
or pages with state that can be opened directly with the URL.

With this, the user can jump into the middle of the application to open a parameterized page.

When opened the class de-serializes the URL and puts the argument instance into `argsOrNull`.

`argsOrNull` contains either:

- an arguments instance, or
- null, when there are arguments in the URL or the URL is mal-formed

Property `args` is a null-safe access to the arguments instance, it throws an 
exception on read when `argsOrNull` is null.

By default, the class converts malformed URLs into `null` arguments. To change this
behaviour override the `hideUrlError` property in the extending class:

```kotlin
override val hideUrlError = false
```