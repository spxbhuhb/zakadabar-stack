# Layouts

Layouts define the general structure for the UI. Typically, there are only a few layouts for an application: full screen, one with the side menu, one for mobile.

For each page there is a layout that page uses to when displayed. For example the application might not want to display the menu before the user logs in. In this case the login page uses the full
screen layout.

All layouts extend [AppLayout](./AppLayout.kt).

When the user performs navigation, the [Application](./Application.kt)
tries to find the page that is able to display the new location.

These pages (ones that can display a location) are called targets, they implement [AppRouting.ZkTarget](./AppRouting.kt).

Each target has a layout associated with it. When the layout is **NOT** the same as the one currently displayed the application switches to the other layout. Then it calls the `resume` function of the
layout to load the given target.

The layouts themselves are never removed, they are only hidden when not active. The application calls `pause` of the layout when it is replaced with another and 'resume' when it is activated.

# Routing

Routing handles URL changes:

* initial page load
* back and forward buttons of the browser
* navigation by links
* programmatic navigation

The browser's [Location](https://developer.mozilla.org/en-US/docs/Web/API/Location) determines what is displayed to the user. See [URLs](../common/URLs.md#View-URLs) for more information.

In the [Application](./Application.kt) object:

- the `routing` property stores the routing of the application
- the `back` function steps back in the browser history
- the `changeNavState` function navigates to other page

The [Application](./Application.kt) object registers for the `popstate` event of the browser window and updates the application whenever the URL changes.

## Mechanism

### Page Load

To initialise routing call `Application.init`. This is typically the last call in `main.kt`.

`Application.init`:

1. adds event listener to the `popstate` browser event
1. analyzes [Window.location](https://developer.mozilla.org/en-US/docs/Web/API/Location) and creates a [NavState](./NavState.kt)
1. calls [AppRouting.onNavStateChange](./AppRouting.kt) with the NavState, this updates the UI
1. creates a browser event of type `Application.NAVSTATE_CHANGE` and dispatches it on the browser `window`
1. elements that listen on this event do whatever they want to do

### Everything Else

Navigation happens when:

- the user clicks on an element,
- the user uses the `back` or `forward` function of the browser,
- the code decides to switch the location.

Elements that want to receive navigation events has to add an event listener:

```kotlin
class NavLog : ZkElement() {
    override fun init(): NavLog {
        on(window, Application.NAVSTATE_CHANGE) { console.log(Navigation.state) }
        return this
    }
}
```

## Add-And-Forget Navigation

Add-And-Forget navigation means that you add a clickable element and then forget about it. If the user clicks on it, navigation happens, if the user doesn't click, nothing happens.

TODO

## Programmatic Navigation

TODO
