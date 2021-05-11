# Layout

An application may have any number of layouts. A layout defines the general structure of the page.

* Layout classes extend [ZkAppLayout](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkAppLayout.kt).
* There is one active layout at one time, others are in the DOM but hidden.
* [ZkAppRouting](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkAppRouting.kt) handles layout changes

There are two pre-defined layouts:

* [ZkFullScreenLayout](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/ZkFullScreenLayout.kt)
* [ZkDefaultLayout](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/ZkDefaultLayout.kt)

## The Default Layout

This picture shows the structure of ZkDefaultLayout. Blue class names are the components for the given area.

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Error in The Picture">
TitleBarContainer contains a ZkAppTitleBar, not ZkTitleBar.
</div>

![<img src="default-layout.png" width="800"/>](./default-layout.png)

## Application Title Bar

The default layout has an application title bar which has 4 areas:

* sidebar show button (hidden by default)
* title - title of the page currently shown
* context elements - actions that belong to the current page like table search
* global elements - global actions, shown all the time

Default application title bar
implementation: [ZkAppTitleBar](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/titlebar/ZkTitleBar.kt)

Pages, tables and forms use `ZkApplication.title` to set the title and context elements of the title bar.

All high level elements (ZkPage, ZkArgPage, ZkForm, ZkTable) have the following fields:

* `appTitle` - boolean, when true sets the title bar content (default = true)
* `title` - string, when set this element is assigned to `ZkApplication.title`
* `titleText` - string, when set, a new `ZkAppTitle` instance is created and that instance is assigned
  to `ZkApplication.title`

### Global Elements

Global elements of the application title bar are independent of the specific content displayed and are always shown
(if not explicitly hidden).

Use the `+=` on the application title bar to add a global element.

```kotlin
object DefaultLayout : ZkDefaultLayout() {

  override fun onCreate() {
    super.onCreate()

    appHandle = ZkAppHandle(zke { + Strings.applicationName }, onIconClick = ::onToggleSideBar, onTextClick = { Landing.open() })
    sideBar = SideBar
    titleBar = ZkAppTitleBar(::onToggleSideBar)

    titleBar.globalElements += DarkLightMode(SiteDarkTheme.NAME, SiteLightTheme.NAME)

  }

}
```

### Sidebar show button

This button is tied to the side bar. When the side bar is shown the button is hidden. When the side bar is hidden, the
button is shown. `ZkAppTitleBar` automatically handles these states.