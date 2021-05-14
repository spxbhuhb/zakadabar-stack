
# What's New

On this page we collect the major improvements / changes to give an overview of what's happening. For detailed list of
changes please check the changelog.

## 2021.5.18-SNAPSHOT

* backend
    * add [ContentBackend](/src/jvmMain/kotlin/zakadabar/stack/backend/custom/ContentBackend.kt) to core
    
* browser
    * layout
        * `spanHeader` variation for ZkDefaultLayout,
          see [Spanning The Header](./guides/browser/structure/Layout.md#Spanning-the-Header)
    * sidebar      
        * build from a Markdown text
        * use `a` tag
        * shorthands for adding routing targets
    * buttons
        * shorthands for creating with routing targets
        * use `a` tag when url is present
    * dock
        * basic example works (style needs improvement)
    * forms
        * select has arrow icon at the end
    * tables
        * dark mode support
        * checkbox style
    * documentation
        * [Browser: Introduction](./guides/browser/Introdution.md)
        * [Elements](./guides/browser/structure/Elements.md)
        * [Routing](./guides/browser/structure/Routing.md)
        * [Dock](./guides/browser/builtin/Dock.md)  
        * [Pages](./guides/browser/builtin/Pages.md)
        * [SideBar](./guides/browser/builtin/SideBar.md)    
        * [Prototyping](./guides/browser/util/Prototyping.md)

* documentation
    * from now on, documentation pages will end with a "Timeline" section that contains:
        * the past changes
        * known problems
        * future plans