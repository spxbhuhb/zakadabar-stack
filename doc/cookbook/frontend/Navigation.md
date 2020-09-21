# Navigation

Applications built on Zakadabar Stack are Single Page Applications (SPA) in which so-called 
`pages` and `views` show the content to the user.

The browser's [Location](https://developer.mozilla.org/en-US/docs/Web/API/Location) determines
which views/pages are active and what they show. See [URLs](../common/URLs.md#View-URLs) for
more information.

The [Navigation](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/navigation/Navigation.kt) object:

- stores the current [NavigationState](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/navigation/NavigationState.kt) in its  `state` property
- has functions to easily switch locations
- notifies elements when the location changes
- handles the browser `back` and `forward` button

## Mechanism

Working example: 
- [The Place That Can't Be Found](https://github.com/spxbhuhb/zakadabar-samples/blob/master/01-beginner/the-place-that-cant-be-found/README.md)

### Page Load

To initialise navigation call `Navigation.init`. This is typically the last call in `main.kt`.

`Navigation.init`:

1. adds event listener to the `popstate` browser event
1. analyzes [Window.location](https://developer.mozilla.org/en-US/docs/Web/API/Location) and creates a
   [NavigationState](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/navigation/Navigation.kt)
1. sets `Navigation.state` to contain the newly created navigation state 
1. creates a browser event of type `Navigation.EVENT` and dispatches it on the browser `window`
1. elements that listen on this event add the appropriate `page` or `view` to the frontend, see [DesktopCenter](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/desktop/DesktopCenter.kt)

### Everything Else

Navigation happens when:

- the user clicks on an element,
- the user uses the `back` or `forward` function of the browser,
- the code decides to switch the location.

The [Navigation](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/navigation/Navigation.kt) object handles
all of these cases.

Elements that want to receive navigation events has to add an event listener:

```kotlin
class NavLog : ComplexElement() {
    override fun init() : NavAlert {
        on(window, Navigation.EVENT) { console.log(Navigation.state) }
    }
}
```

## Add-And-Forget Navigation

Add-And-Forget navigation means that you add a clickable element and then forget about it. If the user
clicks on it, navigation happens, if the user doesn't click, nothing happens. 

This is like the good old `<A>` HTML tag. Except we do not use `<A>`.
Browsers tend to add behaviour to `<A>` elements we don't want in an SPA.

Use the [navLink](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/navigation/navLink.kt) functions
for add-and-forget navigation. They create an element and simply attach an event listener to "onclick" that 
will perform the programmatic navigation (see below) when clicked.

**Not yet implemented.**

```kotlin
this build {
    + navLink(RabbitDto.type, 12, Navigation.READ, "Fluff-Bunny")
}
```
**Not yet implemented.**

To use an element instead of simple text:

```kotlin
this build {
    + navLink(RabbitDto.type, 12, Navigation.UPDATE, Icons.edit.simple18)
}
```

Short hand for an anonymous complex element:

**Not yet implemented.**

```kotlin
this build {
    + navLink(RabbitDto.type, 12, Navigation.READ) { ! "<b>Fluff-Bunny</b>" }
}
```

When you have a DTO at hand you may use one of its `*Link` methods. They are shorthands for
[navLink](../../../src/jsMain/kotlin/zakadabar/stack/frontend/builtin/navigation/navLink.kt).

Using elements and/or the shorthand also works.

**Not yet implemented.**

```kotlin
this build {
    + RabbitDto.createLink() // uses t("create"), you may pass your own text
    + dto.readLink() // uses t("read"), you may pass your own text
    + dto.updateLink() // uses t("update"), you may pass your own text
    + dto.deleteLink() // uses t("delete"), you may pass your own text

    + dto.updateLink(Icons.edit.simple18)
    + dto.readLink { ! "<b>Fluff-Bunny</b>" }
}
```

### Navigate To a Page

To navigate to a page, add a navLink with the class.

```kotlin
+ navLink(Tortuga::class, "Tortuga")
```

### Navigate to Other Website

To add navigation that goes to another website use:

**Not yet implemented.**

```kotlin
this build {
    + navLink("https://www.github.com/")
}
```

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

### Step Into a Sub-Data Structure 

**Not yet implemented.**

Go deeper in a hierarchical data structure. This will result in a location with
contains a data path. For example: `/view/eae64d/quest/1/eae64d/cave/2/eae64d/rabbit/3/read`

See [URLs](../common/URLs.md#Nested-Data-Paths) for more information.

```kotlin
// you are at: /view/eae64d/quest/1/eae64d/cave/2/read
Navigation.stepInto(RabbitDto.type, 12, Navigation.READ)
// you went to: /view/eae64d/quest/1/eae64d/cave/2/eae64d/rabbit/12/read
```

### Step Out of a Sub-Data Structure 

**Not yet implemented.**

Go higher in a hierarchical data structure.

See [URLs](../common/URLs.md#Nested-Data-Paths) for more information.

```kotlin
// you are at: /view/eae64d/quest/1/eae64d/cave/2/eae64d/rabbit/12/read
Navigation.stepOut()
// you went to: /view/eae64d/quest/1/eae64d/cave/2/read
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