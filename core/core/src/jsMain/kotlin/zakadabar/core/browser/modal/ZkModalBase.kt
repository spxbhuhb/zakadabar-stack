/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.modal

import kotlinx.coroutines.channels.Channel
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.application
import zakadabar.core.browser.titlebar.ZkLocalTitleBar
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.plusAssign

/**
 * @property  channel    Receives output of the modal dialog (if there is one).
 *                       Pass a nullable type if the dialog may be closed without
 *                       output.
 * @property  titleText  Title of the dialog. Has to be set before [onCreate].
 *                       When not null, buildTitle adds a [ZkLocalTitleBar] that
 *                       contains the title.
 * @property  addButtons When false the dialog button row is not added. Default
 *                       is true.
 */
open class ZkModalBase<T : Any?> : ZkElement() {

    protected val channel = Channel<T>()

    open var titleText: String? = null

    open var addButtons : Boolean = true

    override fun onCreate() {
        classList += zkModalStyles.modal

        + column {

            buildTitle()

            + div(zkModalStyles.content) {
                buildContent()
            }

            if (addButtons) {
                + row(zkModalStyles.buttons) {
                    buildButtons()
                }
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

    /**
     * Opens the dialog and returns immediately. Does not wait until
     * the dialog is closed. You may wait for dialog close and get
     * the return value with `channel.receive()`.
     */
    open fun launch() {
        io {
            run()
        }
    }

    /**
     * Opens the dialog and waits until it is closed. The return value
     * sent by the dialog is the return value of the function.
     */
    open suspend fun run(): T {
        application.modals.show()
        application.modals += this

        val value = channel.receive()

        application.modals -= this
        application.modals.hide()

        return value
    }

}