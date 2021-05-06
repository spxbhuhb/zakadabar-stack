/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

open class ZkMessageDialog(
    open val title: String? = null,
    open val message: String,
    open val okLabel: String = strings.ok.capitalize(),
) : ZkModalBase<Boolean>() {

    override fun onCreate() {
        classList += ZkModalStyles.modal

        + column {
            title?.let {
                + ZkTitleBar(it) css ZkModalStyles.title
            }

            + div(ZkModalStyles.content) {
                + message
            }

            + row(ZkModalStyles.buttons) {
                + ZkButton(okLabel, onClick = ::onOk)
            }
        }
    }

    open fun onOk() = io {
        channel.send(true)
    }
}