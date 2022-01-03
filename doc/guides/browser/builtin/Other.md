# Other

Other components for use on pages, forms, tables.

## ZkChips

Represent an input, attribute or action. It has an optional icon button.

###without button

<div data-zk-enrich="ExampleChips"></div>

```kotlin
+ ZkChips("name")
```

###with button

<div data-zk-enrich="ExampleChipsWithButton"></div>

```kotlin
val cancelIcon by iconSource("""<path d="M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm5 13.59L15.59 17 12 13.41 8.41 17 7 15.59 10.59 12 7 8.41 8.41 7 12 10.59 15.59 7 17 8.41 13.41 12 17 15.59z"/>""")
val button = ZkButton(fill = false, border = false, round = true, flavour = ZkFlavour.Danger, iconSource = cancelIcon ) {
    toastInfo { "clicked on chips" }
}
+ ZkChips(name = "name", button = button )
```

[Source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/other/ExampleChips.kt)

## ZkGeneratedProfilePicture

<div data-zk-enrich="ExampleGeneratedProfilePicture"></div>

```kotlin
+ ZkGeneratedProfilePicture("FirstName LastName")
```

[Source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/other/ExampleGeneratedProfilePicture.kt)