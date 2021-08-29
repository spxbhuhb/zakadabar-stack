# Sidebar

The sidebar is the area on the left that lets the user navigate on the site. The stack contains a base class for side
bars with convenient shorthand functions for building.

With he `group` and `item` functions you can build a tree-like structure.
[SideBarExample](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/sidebar/SideBarExample.kt)
shows how to do this.

```kotlin
class ExampleSideBar : ZkSideBar() {

    override fun onCreate() {
        super.onCreate()

        + group(ExampleSideBarTarget, "group label") {
            + item("item 1.1") { successToast { "Click on 1.1" } }
            + item("item 1.2") { successToast { "Click on 1.2" } }
        }

        + section("section") {
            + item("item 2.1") { toastSuccess { "Click on 2.1" } }
            + item("item 2.2") { toastSuccess { "Click on 2.2" } }
        }
        
        + item(ExampleSideBarTarget)
      
        + item<ExampleSideBarTarget>()
    }

}
```

<div data-zk-enrich="SideBarExample"></div>

The shorthands are quite self-explanatory. More important is what's happening with `ExampleSideBarTarget`. If you check
the source code of the example (link above), you will see that this is a page. Specifically, it is a routing target.

When you pass a routing target to `item`, `group` or`section`, clicking on the text navigates the application to that 
routing target. Basically, you display another page.

Also, when you pass the target, but don't pass a `text`, the stack looks up the `viewName` of the target in
the `stringStore` and uses the translated name as text. As the `viewName` defaults to the class name, we can put
an `ExampleSideBarTarget` entry into the `stringStore` and have automatically translated labels.

In the example above we used `group` with an explicit text while `item` automatically set the text from the target.

You can pass the target two ways: 

- providing an actual instance, this works well when the page is an object.
- using the `<>` syntax, which will look up the target between the known routing targets.

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Static Lookup">

The lookup `<>` functions perform is static. If there is a target at the time you
do the lookup they'll find it, if there is not they won't. We might change this
in the future to make the application mode dynamic.

</div>

## Icons

All `group` and `item` builders have a version with a [ZkIconSource](/core/core/src/jsMain/kotlin/zakadabar/core/resource/ZkIconSource.kt)
as first parameter. These versions add an icon before the text.

Example: [SideBar With Icons](/doc/cookbook/browser/sidebar/icons/recipe.md)

## Sections

Sections are groups that are not closed but minimized and restored. When you minimize a section, the first
letter of `text` is put to the top of the sidebar and the section itself goes hidden. Clicking on the
letter at the top opens the section again.

## Loading From Markdown

It is possible to load a sidebar structure from a Markdown list. In this case the lines has to be properly formatted:

* each line has to follow the pattern: `* [label](link)`
* there may be spaces before the `*`, this is the way to make groups

The loader is not a full-featured markdown parser, but a simple regex that processes the lines one-by-one.

[SideBarMarkdownExample](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/sidebar/SideBarMarkdownExample.kt)
shows the documentation tree of this site.

<div data-zk-enrich="SideBarMarkdownExample"></div>