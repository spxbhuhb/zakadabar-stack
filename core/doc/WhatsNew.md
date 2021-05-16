# What's New

On this page we collect the major improvements / changes to give an overview of
what's happening. For detailed list of changes please check the changelog.

## 2021.5.18-SNAPSHOT

* common
    * string store lookup by normalized key,
      see [Strings](./guides/common/Strings.md)
    * add `ServerDescriptionDto` to `SessionDto` (property: `serverDescription`)
    * 'locale' in account related DTOs is now mandatory
    * documentation
        * [Strings](./guides/common/Strings.md)

* backend
    * introduce a mandatory `zakadabar.server.description.yaml` settings file
    * put content of the `zakadabar.server.description` setting into SessionDto
      responses
    * add [ContentBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/custom/ContentBackend.kt) to core

* browser
    * css
        * operators for adding and removing CSS classes,
          see [ThemesCss](./guides/browser/structure/ThemesCss.md)
    * layout
        * `spanHeader` variation for ZkDefaultLayout,
           see [Spanning The Header](./guides/browser/structure/Layout.md#Spanning-the-Header)
    * sidebar
        * build from Markdown text,
          see [SideBar](./guides/browser/builtin/SideBar.md#Loading-From-Markdown)
        * shorthands for build with routing targets
        * use `a` tag when target or url is given
    * buttons
        * shorthands for create with routing targets
        * use `a` tag when target or url is given
    * dock
        * basic example works (style needs improvement)
    * forms
        * selects have arrow icon
    * tables
        * dark mode support
        * checkboxes now use ZkCheckBox
    * documentation
        * [Browser: Introduction](./guides/browser/Introduction.md)
        * [Elements](./guides/browser/structure/Elements.md)
        * [Routing](./guides/browser/structure/Routing.md)
        * [Dock](./guides/browser/builtin/Dock.md)
        * [Pages](./guides/browser/builtin/Pages.md)
        * [SideBar](./guides/browser/builtin/SideBar.md)
        * [Prototyping](./guides/browser/util/Prototyping.md)

* documentation
    * from now on, documentation pages will end with a "Timeline" section that
      contains:
        * Known Problems
        * Possible Improvements
        * Changes