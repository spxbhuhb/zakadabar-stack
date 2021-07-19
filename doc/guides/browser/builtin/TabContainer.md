# TabContainer

* [ZkTabContainer](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/tabcontainer/ZkTabContainer.kt) provides display for tabbed content,
* [zkTabContainerStyles](/core/core-core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/tabcontainer/zkTabContainerStyles.kt) contains the styles.

Use the `tab` function to add tabs to the container. There are two variations:

* one with a text title and builder function,
* one with a `ZkElement`.

Both versions have additional parameters to modify appearance easily:

| Parameter | CSS class | Description |
| --- | --- | --- |
| scroll | `zkTabContainerStyles.scrolledContent` | Content is scrolled, default: `true`. |
| border | `zkLayoutStyles.fixBorder` | Add a border around the tab, default: `true`. |
| pad | `zkLayoutStyles.pad` | Add padding around the content, default: `true`. |

```kotlin
class Example : ZkElement() {
    override fun onCreate() {
        super.onCreate()

        + ZkTabContainer {

            height = 400

            + tab("First Tab") {
                + "Content of tab 1"
            }

            + tab("Second Tab", scroll = false, border = false, pad = false) {
                + "Content of tab 2"
            }
          
            + tab(NYI())
        }
    }
}
```

Example [full source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/layout/TabContainerExample.kt)

<div data-zk-enrich="TabContainerExample"></div>


## Timeline

## Known Problems

* tab scrolling is not implemented yet
* the container is not responsive

## Possible Improvements

* implement tab scrolling
* make the component responsive

## Changes

* 2021.5.16
    * added example and documentation