# Sidebar

The sidebar is the area on the left that lets the user navigate on the site. The stack contains a base class for side
bars with convenient shorthand functions for building.

With he `group` and `item` functions you can build a tree-like structure.
[SideBarExample](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/sidebar/SideBarExample.kt)
shows how to do this.

```kotlin
class ExampleSideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + group(ExampleSideBarTarget, "group label") {
            + item("item 1.1") { successToast { "Click on 1.1" } }
            + item("item 1.2") { successToast { "Click on 1.2" } }
        }

        + item(ExampleSideBarTarget)
    }

}
```

<div data-zk-enrich="SideBarExample"></div>

The shorthands are quite self-explanatory. More important is what's happening with `ExampleSideBarTarget`. If you check
the source code of the example (link above), you will see that this is a page. Specifically, it is a routing target.

When you pass a routing target to `item` or to `group`, clicking on the text navigates the application to that routing
target. Basically, you display another page.

Also, when you pass the target, but don't pass a `text`, the stack looks up the `viewName` of the target in
the `stringStore` and uses the translated name as text. As the `viewName` defaults to the class name, we can put
an `ExampleSideBarTarget` entry into the `stringStore` and have automatically translated labels.

In the example above we used `group` with an explicit text while `item` automatically set the text from the target.

## Loading From Markdown

It is possible to load a sidebar structure from a Markdown list. In this case the lines has to be properly formatted:

* each line has to follow the pattern: `* [label](link)`
* there may be spaces before the `*`, this is the way to make groups

The loader is not a full-featured markdown parser, but a simple regex that processes the lines one-by-one.

[SideBarMarkdownExample](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/sidebar/SideBarMarkdownExample.kt)
shows the documentation tree of this site.

<div data-zk-enrich="SideBarMarkdownExample"></div>

