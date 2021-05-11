# Buttons

* Use [ZkButton](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/button/ZkButton.kt) to add buttons in different
  shapes and flavours.
* Extend [ZkButtonStyles](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/button/zkButtonStyles.kt) to override
  default styles and dimensions.

Use constructor parameters of [ZkButton](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/button/ZkButton.kt) to
create fine-tuned buttons:

| Parameter | Description |
| ---- | ---- |
| text | The text of the button, may be null when displaying only an icon. |
| iconSource | The icon to show on the left, when null, no icon is shown. |
| flavour | The flavor of the button, sets color set to use. |
| round | When true and text is null, the icon button will be round. Not used when text is not null. |
| fill | When true, fills the button with the colour specified by flavour. When false, the button background is transparent. |
| border | When true, adds border to the button using the flavour color. When false, no border is added. |
| capitalize | When true, capitalize the text. |
| onClick | The function to execute when the button is clicked. |

You can add text, icon and combined buttons. Text buttons have only a label, icon buttons have only an icon, combined
buttons have an icon and a label. Type of the button is automatically selected based on the constructor parameters.

Add a text button:

```kotlin
+ ZkButton("use strings.* or 't' instead of inline text") {
    // runs when clicked, optional
}
```

Add an icon button:

```kotlin
+ ZkButton(ZkIcons.info) {
    // runs when clicked, optional
}
```

Add a combined button:

```kotlin
+ ZkButton("label", ZkIcons.info) {
    // runs when clicked, optional
}
```

## Built-In Variations [source code](../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/button/ButtonExamples.kt)

### Primary

<div data-zk-enrich="ButtonExamples" data-zk-flavour="primary"></div>

### Secondary

<div data-zk-enrich="ButtonExamples" data-zk-flavour="secondary"></div>

### Success

<div data-zk-enrich="ButtonExamples" data-zk-flavour="success"></div>

### Warning

<div data-zk-enrich="ButtonExamples" data-zk-flavour="warning"></div>

### Danger

<div data-zk-enrich="ButtonExamples" data-zk-flavour="danger"></div>

### Info

<div data-zk-enrich="ButtonExamples" data-zk-flavour="info"></div>

### Disabled

<div data-zk-enrich="ButtonExamples" data-zk-flavour="disabled"></div>