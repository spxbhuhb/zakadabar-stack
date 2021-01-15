Code for the web browser frontend.

* [index.html](./resources/index.html) loads first.
* `index.html` loads the application's `.js` file and calls `main` from [main.kt](./kotlin/main.kt).
* `main` sets up and starts the [Application](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/Application.kt)

The [Application](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/Application.kt) is a central object, there is one for a browser window.

# Structure

A typical application has:

* [Layouts](#Layouts)
* [Routing](#Routing)
* [Menu](#Menu)
* [Crud Pages](#Crud-Pages)
* [Non-Curd Pages](#Non-Crud-Pages)
* [Resources](#Resources)

## Layouts

`TL;DR`

Use [DefaultLayout](./kotlin/zakadabar/demo/frontend/DefaultLayout.kt). Check [Login.kt](./kotlin/zakadabar/demo/frontend/pages/misc/Login.kt)
to see how to use other (search for FullScreen).

### Details

Layouts define the general structure for the UI. Typically, there are only a few layouts for an application: full screen, one with the side menu, one for mobile.

For each page there is a layout that page uses to when displayed. For example the application might not want to display the menu before the user logs in. In this case the login page uses the full
screen layout.

All layouts extend [AppLayout](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/Application.kt).

When the user performs navigation, the [Application](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/Application.kt)
tries to find the page that is able to display the new location.

These pages (ones that can display a location) are called targets, they implement [AppRouting.ZkTarget](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/AppRouting.kt).

Each target has a layout associated with it. When the layout is **NOT** the same as the one currently displayed the application switches to the other layout. Then it calls the `resume` function of the
layout to load the given target.

The layouts themselves are never removed, they are only hidden when not active. The application calls `pause` of the layout when it is replaced with another and 'resume' when it is activated.

## Routing

`TL;DR`

Add your pages to [Routing.kt](./kotlin/zakadabar/demo/frontend/Routing.kt).

## Menu

`TL;DR`

Add your menu items to [Menu.kt](./kotlin/zakadabar/demo/frontend/Menu.kt).

## Crud Pages

Extend [ZkCrud](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/ZkCrud.kt) to have the routing and communication.

`TL;DR`

* DTO - [ShipDto](../commonMain/kotlin/zakadabar/demo/data/ShipDto.kt)
* Form - [ShipForm](./kotlin/zakadabar/demo/frontend/pages/ship/ShipForm.kt)
* Table - [ShipTable](./kotlin/zakadabar/demo/frontend/pages/ship/ShipTable.kt)
* Crud - [Ships](./kotlin/zakadabar/demo/frontend/pages/ship/Ships.kt)

## Non-Crud Pages

For non-CRUD pages you can use [ZkPage](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/ZkPage.kt) to have the routing and communication.

`TL;DR`

[Tortuga](./kotlin/zakadabar/demo/frontend/pages/port/Tortuga.kt)

## Resources

`TL;DR`

* [Strings](./kotlin/zakadabar/demo/frontend/resources/Strings.kt)
* [Styles](./kotlin/zakadabar/demo/frontend/resources/Styles.kt)
* [Theme](./kotlin/zakadabar/demo/frontend/resources/Theme.kt)
