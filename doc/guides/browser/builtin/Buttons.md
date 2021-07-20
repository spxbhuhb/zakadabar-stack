# Buttons

* Use [ZkButton](/core/core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/button/ZkButton.kt) to add buttons in different
  shapes and flavours.
* Extend [ZkButtonStyles](/core/core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/button/zkButtonStyles.kt) to override
  default styles and dimensions.

## Shorthands

For basic buttons, simply use one of these:

```kotlin
buttonPrimary("label") { println("clicked") }
buttonSecondary("label") { println("clicked") }
buttonSuccess("label") { println("clicked") }
buttonWarning("label") { println("clicked") }
buttonDanger("label") { println("clicked") }
buttonInfo("label") { println("clicked") }
```

For links, use the target-aware version of the shorthands. These will set the label to the translated target `viewName`
and the url to the url that opens the target.

```kotlin
buttonPrimary(target)
buttonSecondary(target)
buttonSuccess(target)
buttonWarning(target)
buttonDanger(target)
buttonInfo(target)
```

## Button or Link

A quite good description of buttons and links on [stackoverflow](https://stackoverflow.com/a/48433150/3796844)
from [alexggordon](https://stackoverflow.com/users/2506667/alexggordon):

---

Generally, my distinguishing factor is that Buttons execute an action whereas Links navigate you to a different portion
of a website, generally undoable with the back button.

Some common Buttons:

* Save Button (on a form)
* Post Your Answer (stack overflow question answer)
* Log In
* Send Email

Some Common Links:

* Home, Profile (facebook)
* Questions (stack overflow)
* Notifications (twitter)
* Popular (reddit)

As a metric for which to use, I usually like to ask if the thing I'm doing would be able to be undone with the back
button on the browser. You can't "back button" a login request, or a tweet, but you can "back button" navigation to your
profile on facebook or a link on reddit.

---

## Fine Tuning

Use constructor parameters of [ZkButton](/core/core/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/button/ZkButton.kt) to
create fine-tuned buttons:

| Parameter | Description |
| ---- | ---- |
| text | The text of the button, may be null when displaying only an icon. |
| iconSource | The icon to show on the left, when null, no icon is shown. |
| flavour | The flavor of the button, sets color set to use. |
| url | The URL this button opens. This might be a local URL in which case the default onClick calls [application.changeNavState]. When it is not a local URL (starts with https:// or http://), the standard browser mechanism for "a" tag is executed. |
| round | When true and text is null, the icon button will be round. Not used when text is not null. |
| fill | When true, fills the button with the colour specified by flavour. When false, the button background is transparent. |
| border | When true, adds border to the button using the flavour color. When false, no border is added. |
| capitalize | When true, capitalize the text. |
| onClick | The function to execute when the button is clicked. |

You can add text, icon and combined buttons. Text buttons have only a label, icon buttons have only an icon, combined
buttons have an icon and a label. Type of the button is automatically selected based on the constructor parameters.

When URL is passed the button will use an `a` tag. When there is no URL it will use a `div` tag.

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

### Click With Event

`onClick` parameter of the constructor is for functions that don't care about the
event. If you need the event object you can add an event handler with `on`.

This example is for a button that downloads a file. It is good practice using
the `url` parameter, so search engines can follow the link. On the other hand,
we really don't want the default behaviour here.

Note that we passed an empty `onClick`, so default behaviour of `ZkButton` is
also suppressed (it would think that this is a local url and change the nav
state).

```kotlin
+ ZkButton(
  iconSource = ZkIcons.fileDownload,
  flavour = ZkFlavour.Info,
  url = bo.url(),
  onClick = {  }
).on("click") { event ->
  event.preventDefault()
  window.location = bo.url()
}
```

## Built-In Variations [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/button/ButtonExamples.kt)

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

## Timeline

### Changes

* 2021.5.14
    * shorthands for create with routing targets
    * use `a` tag when target or url is given
* 2021.5.12
    * rename convenience functions from buttonXX to XXbutton