Code for the web browser frontend.

* `resources/`[index.html](resources/index.html) loads first.
* `index.html` loads the application's `.js` file and calls `main` from `kotlin/`[main.kt](kotlin/main.kt).
* `main` sets up and starts
  the [Application](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/Application.kt)

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

* [ZkElement](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/README.md#ZkElement) - base class for
  all elements
* [ZkTable](../../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/table)
* [ZkForm](../../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/table)
* [ZkCrud](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/README.md#ZkCrud) - helper to add crud
  routing / handling easily
* [ZkPage](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/elements/README.md#ZkPage) - helper to add non-crud
  routing / handling easily

Apart these there are a number of built in elements such as buttons, inputs and so on.

## Layouts

`Details`
in [application.Layouts](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/README.md#Layouts)

Use [zakadabar.demo.frontend.DefaultLayout](../../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/layout/DefaultLayout.kt)
. Check [Login.kt](kotlin/zakadabar/demo/marina/frontend/pages/misc/Login.kt)
to see how to use other (search for FullScreen).

## Routing

`Details`
in [application.Routing](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/application/README.md#Routing)

Add your pages to [Routing.kt](kotlin/zakadabar/demo/marina/frontend/Routing.kt).

## Menu

Add your menu items to [Menu.kt](kotlin/zakadabar/demo/marina/frontend/Menu.kt).

## Crud Pages

* Extend [ZkCrud](../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/ZkCrud.kt).
* Add it to [Routing.kt](kotlin/zakadabar/demo/marina/frontend/Routing.kt).
* Add it to [Menu.kt](kotlin/zakadabar/demo/marina/frontend/Menu.kt).

`Example`

* DTO - [ShipDto](../commonMain/kotlin/zakadabar/demo/marina/data/ship/ShipDto.kt)
* Form - [ShipForm](kotlin/zakadabar/demo/marina/frontend/pages/ship/ShipForm.kt)
* Table - [ShipTable](kotlin/zakadabar/demo/marina/frontend/pages/ship/ShipTable.kt)
* Crud - [Ships](kotlin/zakadabar/demo/marina/frontend/pages/ship/Ships.kt)

## Non-Crud Pages

* Extend [ZkPage](../../../../core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/pages/ZkPage.kt).
* Add it to [Routing.kt](kotlin/zakadabar/demo/marina/frontend/Routing.kt).
* Add it to [Menu.kt](kotlin/zakadabar/demo/marina/frontend/Menu.kt).

`Example`

[Tortuga](kotlin/zakadabar/demo/marina/frontend/pages/port/Tortuga.kt)

## Resources

* [Strings](../commonMain/kotlin/zakadabar/demo/resources/demo.kt)
* [Styles](../commonMain/kotlin/zakadabar/demo/resources/Styles.kt)
* [Theme](../commonMain/kotlin/zakadabar/demo/resources/Theme.kt)