# Toasts

Toasts are small pop-up messages displayed to the user to inform him about something.

* Use [toast functions](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/toast/toast.kt) to display simple toasts.
* Use [ZkToast](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/toast/ZkToast.kt) to display fine-tuned toasts.
* Extend [ZkToastStyles](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/toast/ZkToastStyles.kt) to override default
  styles.
* Toasts are added to the `toasts` property
  of [ZkApplication](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt) by convenience functions.
* The `toast` property is an instance
  of [ZkToastContainer](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/toast/ZkToastContainer.kt).

<div data-zk-enrich="InfoNote" data-zk-title="Auto Hide">
Primary, secondary, success and info toasts are hidden after 3 seconds.

Warning and danger toasts are kept on the screen until the user closes them manually.
</div>

## Use Toasts

```kotlin
successToast { "This is a success!" }
```

Toast convenience functions:

```kotlin
primaryToast { "message" }
secondaryToast { "message" }
successToast { "message" }
warningToast { "message" }
dangerToast { "message" }
infoToast { "message" }
```

Use constructor parameters of [ZkToast](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/toast/ZkToast.kt) to create
fine-tuned toasts.

| Parameter | Description |
| ---- | ---- |
| text | The text to display. When null, `content` should be set. |
| content | The content to display. When null, `text` should be set. |
| flavour |  Flavour of the toast, sets coloring. |
| hideAfter | Number of milliseconds after the toast is automatically hidden. |

To display a toast built from  [ZkToast](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/toast/ZkToast.kt) use
the `run` method:

```kotlin
ZkToast("message").run()
```

## Built-In Toasts [source code](../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/toast/ToastExamples.kt)

Click on the button to display the toast.

<div data-zk-enrich="ToastExamples"></div>

## Complex Toast Content [source code](../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/toast/ToastFormExample.kt)

* This example shows how to use a complex element (a form in this case) for a toast.
* Auto-hiding is disabled to let the user process the content of the toast.
* To show the toast create an instance and call `run`:

```kotlin
ZkToast(content = InlineForm(), hideAfter = null).run()
```

<div data-zk-enrich="WarningNote" data-zk-title="Toast Content Buttons">
Pay attention to the default actions when adding a toast content.
Form buttons might not work with default settings.
For example: "Back" might redirect the underlying page.
</div>

<div data-zk-enrich="ToastFormExample"></div>