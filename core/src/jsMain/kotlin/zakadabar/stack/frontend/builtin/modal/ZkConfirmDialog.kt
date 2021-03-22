/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import kotlinx.coroutines.channels.Channel
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.builtin.ZkBuiltinStrings.Companion.builtin
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

open class ZkConfirmDialog(
    val title: String? = null,
    val message: String,
    val noLabel: String = builtin.no.capitalize(),
    val yesLabel: String = builtin.yes.capitalize(),
) : ZkElement() {

    val channel = Channel<Boolean>()

    override fun onCreate() {
        classList += ZkModalStyles.modal

        + column {
            if (title != null) {
                + ZkTitleBar(title) css ZkModalStyles.title
            }

            + div(ZkModalStyles.content) {
                + message
            }

            + row(ZkModalStyles.buttons) {
                + ZkButton(noLabel, ::onNo)
                + ZkButton(yesLabel, ::onYes)
            }
        }
    }

    open suspend fun run(): Boolean {
        ZkApplication.modals.show()
        ZkApplication.modals += this

        val value = channel.receive()

        ZkApplication.modals -= this
        ZkApplication.modals.hide()

        return value
    }

    open fun onNo() = io {
        channel.send(false)
    }

    open fun onYes() = io {
        channel.send(true)
    }
}