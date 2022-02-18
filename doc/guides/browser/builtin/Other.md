# Other

Other components for use on pages, forms, tables.

## ZkChip

Represent an input, attribute or action. It has an optional icon button.

### Text Only

<div data-zk-enrich="ExampleChip"></div>

```kotlin
+ ZkChip("primary")
```

### With Cancel Button

<div data-zk-enrich="ExampleChipWithButton"></div>

```kotlin
+ ZkChip("primary", cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } }
```

### With Icon

<div data-zk-enrich="ExampleChipWithIcon"></div>

```kotlin
+ ZkChip("primary", icon = ZkIcons.globe)
```

### With Icon And Cancel Button

<div data-zk-enrich="ExampleChipWithIconAndButton"></div>

```kotlin
+ ZkChip("primary", icon = ZkIcons.globe, cancelIcon = ZkIcons.cancelIcon) { toastSuccess { "click" } }
```

[Source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/other/ExampleChip.kt)

## ZkGeneratedProfilePicture

<div data-zk-enrich="ExampleGeneratedProfilePicture"></div>

```kotlin
+ ZkGeneratedProfilePicture("FirstName LastName")
```

[Source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/other/ExampleGeneratedProfilePicture.kt)