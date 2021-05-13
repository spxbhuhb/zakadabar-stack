# Routing

## Details

The routing handles URL changes. Those happen at

* initial page load,
* back and forward buttons of the browser,
* navigation by links,
* programmatic navigation.

The browser's [Location](https://developer.mozilla.org/en-US/docs/Web/API/Location) determines what is displayed to the
user. See [URLs](../../common/URLs.md) for more information.

Important points:

* `application` ([ZkApplication](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt))
   * `routing` property stores the routing of the application
   * handles the `back` and `forward` button of the browser
   * provides functions to switch locations
   * notifies elements when the location changes
* [ZkAppRouting](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkAppRouting.kt)
   * is extended by application-specific routing classes
   * `navState` property stores the current navigation state, a [ZkNavState](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkNavState.kt)
   * `targets` property stores the known routing targets
* [ZkAppRouting.ZkTarget] the interface routing targets implement, see details below
* `/api/` URL is reserved for backend communication    

## Mechanism

### Page Load

1. `main.kt` sets the `routing` property of the application.
1. `application.init`, called from `main.kt` initializes the routing.
    1. Add an event listener to the `popstate` browser event.
    1. Analyze [Window.location](https://developer.mozilla.org/en-US/docs/Web/API/Location) and create a
       [ZkNavState](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkNavState.kt).
    1. Call routing to route the application to the given navigation state.
    1. Create a browser event of type `zk-navstate-change` and dispatches it on the browser `window`

### Everything Else

Routing happens when:

* the user clicks on an element,
* the user uses the `back` or `forward` function of the browser,
* the code decides to switch the location.

All of these cases are handled by ZkAppRouting (more specifically, by the application dependent class that extends ZkAppRouting).

Elements that want to receive navigation events has to add an event listener:

```kotlin
class NavLog : ZkElement() {
    override fun onCreate() {
        on(window, application.NAVSTATE_CHANGE) { console.log(ZkApplication.routing.navState) }
    }
}
```

## Write a Routing

* Extend the ZkAppRouting class.
* Add target objects and/or instances with the `+` operator.
* The first parameter is the default layout to use.
* The second parameter is the home page.

```kotlin
class Routing : ZkAppRouting(DefaultLayout, Landing) {
    init {
        + Landing
        + WhatsNew()
    }
}
```

Set the routing (typically in `main.kt`).

```kotlin
with (ZkApplication) {
    routing = Routing()
}
```

## Targets

Routing targets implement the [ZkAppRouting.ZkTarget](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkAppRouting.kt) interface.

This interface is pretty straightforward:

```kotlin
    interface ZkTarget {
        val viewName: String
        fun route(routing: ZkAppRouting, state: ZkNavState): ZkElement
    }
```

`viewName` is the second segment of the URL. For example the view name in
`https://zakadabar.io/en/Documentation/guides/backend/HttpServer.md` is `Documentation`.

The routing process is as follows.

* Create a nav state (decomposed URL, [ZkNavState](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkNavState.kt).
* Find the target for the view name in the nav state.
* Call `route` function of the target. `route`
    * finds or creates a ZkElement to display,
    * sets the desired layout in `ZkAppRouting.nextLayout`.
* Remove the active target from the active layout.    
* If necessary switch the layout.
* Add the new target to the active layout.

<div data-zk-enrich="Note" data-zk-flavour="Success" data-zk-title="No Hierarchy">
The application routing does not handle hierarchies. Each target may implement
a complex routing strategy in their route method, but the application itself uses
the view name only.
</div>

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Layout">
The layout to use is selected by the target. Most cases you don't want to touch this,
but login screens for example tend to use a different layout. See the built-in login page
for an example.
</div>

## Add-And-Forget Navigation

Add-And-Forget navigation means that you add a clickable element and then forget about it. If the user clicks on it,
navigation happens, if the user doesn't click, nothing happens.



## Programmatic Navigation

[Pages](../builtin/Pages.md) usually have an `open` function. When you call this the
given page opens.

```kotlin
Welcome.open()
```

[Crud](../builtin/Crud.md) provides multiple open functions.

| Function | Result |
| `openAll()` | A table of records opens. |
| `openCreate()` | A form opens to create a new record. |
| `openRead(id : RecordId<T>)` | A form opens for the given record in read mode. |
| `openUpdate(id : RecordId<T>)` | A form opens for the given record in update mode. |
| `openDelete(id : RecordId<T>)` | A form opens for the given record in delete mode. |

You can find a target by looking it up in the `targets` property of the routing or
by using the `find` method of the routing.

```kotlin
application.routing.find<MyTargetClass>().findFirst().open()
```

Use `application.back` to step back in the browser history. The stack will handle the navigation state
change and load the target that belongs to the new state.

```kotlin
application.back()
```

Use `application.changeNavState` to route the application to any target.

```kotlin
application.changeNavState(target)
```

Use `application.changeNavState` to route the application to any given path and query.

```kotlin
application.changeNavState("/Welcome","")
```
