/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.modal

import kotlinx.coroutines.channels.Channel
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.titlebar.ZkLocalTitleBar
import zakadabar.stack.frontend.util.plusAssign

open class ZkModalBase<T : Any?> : ZkElement() {

    protected val channel = Channel<T>()

    open var titleText: String? = null

    override fun onCreate() {
        classList += zkModalStyles.modal

        + column {

            buildTitle()

            + div(zkModalStyles.content) {
                buildContent()
            }

            + row(zkModalStyles.buttons) {
                buildButtons()
            }
        }
    }

    open fun buildTitle() {
        titleText?.let {
            + ZkLocalTitleBar(it) css zkModalStyles.title
        }
    }

    open fun buildContent() {

    }

    open fun buildButtons() {

    }

    open suspend fun run(): T {
        application.modals.show()
        application.modals += this

        val value = channel.receive()

        application.modals -= this
        application.modals.hide()

        return value
    }

}