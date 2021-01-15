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

## Programmatic Navigation

### Change the Location

To change the location to another one. Usually independent of the current location.

```kotlin
val dto = RabbitDto.read(12)
Navigation.changeLocation(toDto, Navigation.READ)
```

Or use a shorthand:

```kotlin
Navigation.changeLocation(Navigation.READ) { RabbitDto.read(12) }
```

### Change the View

**Not yet implemented.**

Change the view on the same data.

```kotlin
Navigation.changeView(Navigation.UPDATE)
```

### Go Back

**Not yet implemented.**

To go back to the previous location.

- This call may cause the browser to leave your application.
- Executed also when the user clicks on the "Back" button of the browser.

```kotlin
Navigation.stepBack()
```

### Go Forward

**Not yet implemented.**

To go forward to a location you've seen before using `stepBack`.

- This call may cause the browser to leave your application.
- Executed also when the user clicks on the "Forward" button of the browser.

```kotlin
Navigation.stepForward()
```

## Building Navigation for an Application

**Not yet implemented.**

```kotlin
Navigation.build {
    group("header") {
        item("header-item-1")
        item("header-item-2")
        item("header-item-3")
    }
    group("menu") {
        item("menu-item-1", role = Roles.Administrator) {
            + DtoBackend1::class
            + DtoBackend2::class
        }
        item("menu-item-2", role = Roles.SiteMember) {
            + AccountPage::class
        }
        item("menu-item-3", role = Roles.Anonymous) {
            + LoginPage::class
        }
    }
}
```