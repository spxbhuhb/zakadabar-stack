# Modals

Modal dialogs are handled through the `modals` property of
the `application` ([ZkApplication](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt)).
The `modals` property is an instance
of [ZkModalContainer](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/modal/ZkModalContainer.kt).

Modals that extend [ZkModalBase](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/modal/ZkModalBase.kt)
have show, hide and data handling out-of-the-box.

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Note">
Support for multiple parallel modals is not ready yet.
</div>

## Built-In Modals [source code](../../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/modal/ModalExamples.kt)

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

* extend [ZkModalBase](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/modal/ZkModalBase.kt)
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
        classList += ZkModalStyles.modal

        + column {
            + div(ZkModalStyles.content) {
                + "This is my message dialog."
            }

            + row(ZkModalStyles.buttons) {
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

Modals that extend [ZkModalBase](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/modal/ZkModalBase.kt) has a `run`
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

## Timeline

### Changes

* 2021.6.10
    * Add `withConfirm` shorthand.
    * Fix build based example.
* 2021.5.14
    * Add build function to make styled modals easily.
* 2021.5.12
    * Change opacity of overlay background from 0.2 to 0.5 to make the modal more distinct.