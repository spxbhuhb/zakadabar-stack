# Modals

Modal dialogs are handled through the `modals` property of
the `application` ([ZkApplication](/core/core/src/jsMain/kotlin/zakadabar/core/browser/application/ZkApplication.kt)).
The `modals` property is an instance
of [ZkModalContainer](/core/core/src/jsMain/kotlin/zakadabar/core/browser/modal/ZkModalContainer.kt).

Modals that extend [ZkModalBase](/core/core/src/jsMain/kotlin/zakadabar/core/browser/modal/ZkModalBase.kt)
have show, hide and data handling out-of-the-box.

## Built-In Modals [source code](/lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/modal/ModalExamples.kt)

<div data-zk-enrich="ModalExamples"></div>

## Shorthands

### withConfirm

This shorthand shows a confirmation dialog and if the user confirmed the action, executes the
passed function.

```kotlin
withConfirm {
    println("Action executed!")
}
```

The default implementation uses `localizedStrings.confirmation` as title and `localizedStrings.confirmDelete`
as message. To change this, pass a title and/or a message:

```kotlin
withConfirm(strings.title, strings.message) {
    println("Action executed!")
}
```

## Write a Modal With Build

In this case the modal will be similar to the built-in modals: same styled title, content, buttons.

* extend [ZkModalBase](/core/core/src/jsMain/kotlin/zakadabar/core/browser/modal/ZkModalBase.kt)
* call `build` from `onCreate` with the title
* override `buildContent` to build the content
* override `buildButtons` to build the buttons

```kotlin
open class MyMessageDialog : ZkModalBase<Boolean>() {
  
    override fun buildTitle() {
        + "My Title"
    }

    override fun buildContent() {
        + "My Message"
    }

    override fun buildButtons() {
        + ZkButton(okLabel, onClick = ::onOk)
    }

    open fun onOk() = io {
        channel.send(true)
    }

}
```

## Write a Modal Without Build

In this case you are free to put whatever into the modal.

* extend[ZkModalBase](/ src / jsMain / kotlin / zakadabar / stack / frontend / builtin / modal / ZkModalBase . kt)
* build the modal body in `onCreate`
* send a value into `channel` when the modal should be closed

```kotlin
open class MyMessageDialog : ZkModalBase<String>() {

    override fun onCreate() {
        classList += zkModalStyles.modal

        + column {
            + div(zkModalStyles.content) {
                + "This is my message dialog."
            }

            + row(zkModalStyles.buttons) {
                + ZkButton("I will use translated strings instead", onClick = ::onOk)
            }
        }
    }

    open fun onOk() = io {
        channel.send("You promised!")
    }
}
```

## Use a Modal

Modals that extend [ZkModalBase](/core/core/src/jsMain/kotlin/zakadabar/core/browser/modal/ZkModalBase.kt) has a `run`
method. When called, `run`:

* calls `show` of `application.modals`
* adds the modal to `application.modals`
* waits until a value arrives in the channel
* removes the modal from `application.modals`
* calls `hide` of application.modals

To show the modal use:

```kotlin
io {
    val output = MyMessageDialog().run()
    // do something with the output if needed
}
```

## Multiple Modals

It is possible to open more than one modal at the same time.

Consequent modals are placed over previous ones and have to be closed
before the previous modals are closed.

In other words, only the latest modal is active until it is closed.

For an example check the [Open a Form in a Modal Dialog](/doc/cookbook/browser/form/modal/recipe.md).
(Click on `Open`, `Execute`, `Back`, in this order.)