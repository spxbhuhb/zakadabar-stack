# Modals

Modal dialogs are handled through the `modals` property
of [ZkApplication](/src/jsMain/kotlin/zakadabar/stack/frontend/application/ZkApplication.kt). The `modals` property is
an instance of [ZkModalContainer](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/modal/ZkModalContainer.kt).

Modals that extend [ZkModalBase](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/modal/ZkModalBase.kt)
have show, hide and data handling out-of-the-box.

<div data-zk-enrich="Note" data-zk-flavour="Info" data-zk-title="Note">
Support for multiple parallel modals is not ready yet.
</div>

## Built-In Modals [source code](../../../../lib/examples/src/jsMain/kotlin/zakadabar/lib/examples/frontend/modal/ModalExamples.kt)

<div data-zk-enrich="ModalExamples"></div>

## Write a Modal

* extend [ZkModalBase](/src/jsMain/kotlin/zakadabar/stack/frontend/builtin/modal/ZkModalBase.kt)
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

* calls `show` of `ZkApplication.modals`
* adds the modal to `ZkApplication.modals`
* waits until a value arrives in the channel
* removes the modal from `ZkApplication.modals`
* calls `hide` of ZkApplication.modals

To show the modal use:

```kotlin
io {
    val output = MyMessageDialog().run()
    // do something with the output if needed
}
```