# Dock

The dock contains docked items and shows them over the normal content. The items
may be minimized, normal sized or maximized. Useful to pick out elements from the 
normal document flow, mostly for editing.

The FrontendContext adds the dock when its `init` method runs.

The dock is available in the `dock` field of FrontendContext.

**IMPORTANT** As docked elements are outside the normal element structure you have
to remove them manually.

Technically you can add any elements to the dock but there is a DockItem element 
which covers minimize, maximize and close.

## Add the Dock

Usually there is one dock instance per frontend that is created **but not added** by
`FrontendContext.init`.

To add the dock modify `main.kt` in `jsMain`.

```kotlin
FrontendContext.init()

document.body?.appendChild(FrontendContext.dock.element)
```

## Dock an Element

```kotlin
val helloWorld = SimpleText("Hello World!").grow().build {
    current.style.backgroundColor = "gray"
}

FrontendContext.dock += DockedElement(Icons.menu.simple16, "hello", DockItemState.Minimized, helloWorld)
```

## Remove a Docked Element

If you use DockedItem the user can remove the docked element by clicking on the "close" icon.

The close icon calls `onClose` of the DockItem.

To remove the docked item manually you can remove the docked element itself **or** the content of the docked element.

```kotlin
val helloWorld = SimpleText("Hello World!").grow().build {
    current.style.backgroundColor = "gray"
}

FrontendContext.dock += DockedElement(Icons.menu.simple16, "hello", DockItemState.Minimized, helloWorld)

FrontendContext.dock -= helloWorld
```

```kotlin
val helloWorld = SimpleText("Hello World!").grow().build {
    current.style.backgroundColor = "gray"
}

val dockedHelloWorld = DockedElement(Icons.menu.simple16, "hello", DockItemState.Minimized, dockedHelloWorld)

FrontendContext.dock += dockedHelloWorld

FrontendContext.dock -= dockedHelloWorld
```