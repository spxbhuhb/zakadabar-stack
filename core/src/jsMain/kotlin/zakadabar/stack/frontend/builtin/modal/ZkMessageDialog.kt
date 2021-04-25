/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import kotlinx.coroutines.channels.Channel
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

open class ZkMessageDialog(
    open val title: String? = null,
    open val message: String,
    open val okLabel: String = strings.ok.capitalize(),
) : ZkElement() {

    private val channel = Channel<Boolean>()

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

    open suspend fun run(): Boolean {
        ZkApplication.modals.show()
        ZkApplication.modals += this

        val value = channel.receive()

        ZkApplication.modals -= this
        ZkApplication.modals.hide()

        return value
    }

    open fun onOk() = io {
        channel.send(true)
    }
}