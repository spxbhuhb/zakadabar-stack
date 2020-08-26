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

## Dock an Element

```kotlin
val dockedHelloWorld = SimpleText("Hello World!").grow().build {
    current.style.backgroundColor = "gray"
}

FrontendContext.dock += DockElement(Icons.menu.simple16, "hello", DockItemState.Minimized, dockedHelloWorld)
```

## Remove a Docked Element

If you use DockedItem the user can remove the docked element by clicking on the "close" icon.

The close icon calls `onClose` of the DockItem.

```
dock -= dockedHelloWorld
```
