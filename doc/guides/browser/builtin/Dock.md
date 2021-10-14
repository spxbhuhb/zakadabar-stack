# Dock

<div data-zk-enrich="Note" data-zk-flavour="Danger" data-zk-title="Work In Progress">
The dock is not finished yet.
</div>

The dock contains docked items and shows them over the normal content. The items
may be minimized, normal-sized or maximized. Useful to pick out elements from the 
normal document flow, mostly for editing.

The `application` adds the dock when its `init` method runs.

The dock is available in the `dock` field of `application`.

Technically you can add any elements to the dock but there is a DockItem element 
which provides minimize, maximize and close.

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Page Refresh">
Docked elements disappear if the user refreshes the page. This should rarely
happen in an SPA application, so we don't plan to address this.
</div>

## Add the Dock

Usually there is one dock instance per frontend that is created and added by
`application.init`.

## Dock an Element

The easiest way is to use the `dock` function of ZkElement.

```kotlin
zke { + "Hello World!" }.dock(ZkIcons.menu, "hello")
```

<div data-zk-enrich="DockBasicExample"></div>

## Remove a Docked Element

If you use ZkDockedElement, the user can remove the docked element by clicking on the "close" icon.

The close icon calls `onClose` of the DockedItem.

To remove the docked item programmatically subtract it from `application.dock`.

```kotlin
val docked = zke { + "hello world" }.dock(ZkIcons.menu, "hello")

application.dock -= docked
```

<div data-zk-enrich="DockRemoveExample"></div>
