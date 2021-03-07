Code for the web browser frontend.

* `resources/`[index.html](./resources/index.html) loads first.
* `index.html` loads the application's `.js` file and calls `main` from `kotlin/`[main.kt](./kotlin/main.kt).
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

Also, you will need code pieces to build the ui, these are called elements. There are a few fundamental element classes to know about:

* [ZkElement](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/README.md#ZkElement) - base class for all elements
* [ZkTable](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/table)
* [ZkForm](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/table)
* [ZkCrud](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/README.md#ZkCrud) - helper to add crud routing / handling easily
* [ZkPage](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/README.md#ZkPage) - helper to add non-crud routing / handling easily

Apart these there are a number of built in elements such as buttons, inputs and so on.

## Layouts

`Details` in [application.Layouts](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/README.md#Layouts)

Use [DefaultLayout](./kotlin/zakadabar/demo/frontend/DefaultLayout.kt). Check [Login.kt](./kotlin/zakadabar/demo/frontend/pages/misc/Login.kt)
to see how to use other (search for FullScreen).

## Routing

`Details` in [application.Routing](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/README.md#Routing)

Add your pages to [Routing.kt](./kotlin/zakadabar/demo/frontend/Routing.kt).

## Menu

Add your menu items to [Menu.kt](./kotlin/zakadabar/demo/frontend/Menu.kt).

## Crud Pages

* Extend [ZkCrud](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkCrud.kt).
* Add it to [Routing.kt](./kotlin/zakadabar/demo/frontend/Routing.kt).
* Add it to [Menu.kt](./kotlin/zakadabar/demo/frontend/Menu.kt).

`Example`

* DTO - [ShipDto](../commonMain/kotlin/zakadabar/demo/data/ship/ShipDto.kt)
* Form - [ShipForm](./kotlin/zakadabar/demo/frontend/pages/ship/ShipForm.kt)
* Table - [ShipTable](./kotlin/zakadabar/demo/frontend/pages/ship/ShipTable.kt)
* Crud - [Ships](./kotlin/zakadabar/demo/frontend/pages/ship/Ships.kt)

## Non-Crud Pages

* Extend [ZkPage](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/ZkPage.kt).
* Add it to [Routing.kt](./kotlin/zakadabar/demo/frontend/Routing.kt).
* Add it to [Menu.kt](./kotlin/zakadabar/demo/frontend/Menu.kt).

`Example`

[Tortuga](./kotlin/zakadabar/demo/frontend/pages/port/Tortuga.kt)

## Resources

* [Strings](./kotlin/zakadabar/demo/frontend/resources/demo.kt)
* [Styles](./kotlin/zakadabar/demo/frontend/resources/Styles.kt)
* [Theme](./kotlin/zakadabar/demo/frontend/resources/Theme.kt)