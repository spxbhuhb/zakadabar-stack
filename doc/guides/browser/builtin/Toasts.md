# Toasts

Toasts are small pop-up messages displayed to the user to provide information about something.

* Use [toast convenience functions](/core/core/src/jsMain/kotlin/zakadabar/core/browser/toast/toast.kt) to display basic
  toasts.
* Use [ZkToast](/core/core/src/jsMain/kotlin/zakadabar/core/browser/toast/ZkToast.kt) to display fine-tuned toasts.
* Extend [zkToastStyles](/core/core/src/jsMain/kotlin/zakadabar/core/browser/toast/zkToastStyles.kt) to override default
  styles.
* Toasts are added to the `toasts` property
  of the `application` ([ZkApplication](/core/core/src/jsMain/kotlin/zakadabar/core/browser/application/ZkApplication.kt)).
* The `toast` property is an instance
  of [ZkToastContainer](/core/core/src/jsMain/kotlin/zakadabar/core/browser/toast/ZkToastContainer.kt).

For basic toasts, simply call the appropriate convenience function:

```kotlin
toastSuccess { "This is a success!" }
```

All convenience support the `hideAfter` parameter. `0` value turns off auto-hide.

```kotlin
toastSuccess(hideAfter = 0) { "This is a success!" }
```

Toast convenience functions:

```kotlin
toastPrimary { "message" }
toastSecondary { "message" }
toastSuccess { "message" }
toastWarning { "message" }
toastDanger { "message" }
toastInfo { "message" }
```

Use constructor parameters of [ZkToast](/core/core/src/jsMain/kotlin/zakadabar/core/browser/toast/ZkToast.kt) to create
fine-tuned toasts.

| Parameter | Description |
| ---- | ---- |
| text | The text to display. When null, `content` should be set. |
| content | The content to display. When null, `text` should be set. |
| flavour |  Flavour of the toast, sets coloring. |
| hideAfter | Number of milliseconds after the toast is automatically hidden. |
| icon |   The icon to use in the toast, overrides flavour icon. |
| iconClass | The CSS class to add to the icon, overrides flavour icon class. |
| innerClass | The CSS class to add to the inner container, overrides flavour inner class. |

To display a toast built from  [ZkToast](/core/core/src/jsMain/kotlin/zakadabar/core/browser/toast/ZkToast.kt) use
the `run` method:

```kotlin
ZkToast("message").run()
```

## Auto Hide [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/toast/ToastAutoHideExample.kt)

Toasts may disappear automatically after a while. The mechanism works as follows:

* When the `hideAfter` parameter of the constructor is set:
  * if its value is `0`, auto hide is off,
  * if its value is `null`, defaults is used,
  * otherwise, it specifies the number of milliseconds until auto-hide.
* otherwise, default values are used.

[ZkToast.autoHideDefaults](/core/core/src/jsMain/kotlin/zakadabar/core/browser/toast/ZkToast.kt) contains the default
settings for each flavour:

| Flavour | Auto Hide |
| --- | --- |
| Primary | off |
| Secondary | 3 seconds |
| Success | 3 seconds |
| Warning | off |
| Danger | off |
| Info | off |
| Custom | off |

To override the default auto-hide values change `autoHideDefaults`, remember it is milliseconds:

```kotlin
ZkToast.autoHideDefaults[ZkFlavour.Info] = 3000
```

Try it out, set a timeout value click on the button:

<div data-zk-enrich="ToastAutoHideExample"></div>

## Built-In Toasts [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/toast/ToastBasicExamples.kt)

<div data-zk-enrich="ToastBasicExamples"></div>

## Custom Toasts [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/toast/ToastCustomExample.kt)

You can customize the icon and the colors easily by setting the appropriate constructor parameters. For example styles
check the source code of the example
or [ZkToastStyles](/core/core/src/jsMain/kotlin/zakadabar/core/browser/toast/zkToastStyles.kt).

```kotlin
ZkToast(
  "This is custom toast!",
  flavour = ZkFlavour.Custom,
  icon = ZkIcon(ZkIcons.cloudUpload),
  iconClass = customToastStyles.customIcon,
  innerClass = customToastStyles.customInner
).run()
```

<div data-zk-enrich="ToastCustomExample"></div>

## Complex Toast Content [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/toast/ToastFormExample.kt)

* This example shows how to use a complex element (a form in this case) for a toast.
* Auto-hiding is disabled to let the user process the content of the toast.
* To show the toast create an instance and call `run`:

```kotlin
ZkToast(content = InlineForm(), hideAfter = null).run()
```

<div data-zk-enrich="Note" data-zk-flavour="Warning" data-zk-title="Toast Content Buttons">
Pay attention to the default actions when adding a toast content.
Form buttons might not work with default settings.
For example: "Back" might redirect the underlying page.
</div>

<div data-zk-enrich="ToastFormExample"></div>

## Timeline

* 2021.5.12
  * rename convenience functions from toastXX to XXtoast
