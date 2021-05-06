/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.frontend.modal

import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.modal.ZkModalBase
import zakadabar.stack.frontend.builtin.modal.ZkModalStyles
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

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