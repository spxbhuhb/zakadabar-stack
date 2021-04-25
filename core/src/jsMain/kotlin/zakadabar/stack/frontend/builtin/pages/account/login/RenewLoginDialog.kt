/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.login

import kotlinx.browser.window
import kotlinx.coroutines.channels.Channel
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkApplication.executor
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.modal.ZkModalStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBar
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.plusAssign

/**
 * Shows a login dialog to let the user renew his/her session.
 *
 * The renew has to leave account information unchanged to keep
 * the consistency of the UI. Therefore it does not call
 * refresh of the sessionManager.
 */
class RenewLoginDialog : ZkElement() {

    private val channel = Channel<Boolean>()

    suspend fun run() {
        ZkApplication.modals.show()
        ZkApplication.modals += this

        channel.receive()

        ZkApplication.modals -= this
        ZkApplication.modals.hide()
    }

    override fun onCreate() {
        classList += ZkModalStyles.modal

        + ZkTitleBar(strings.applicationName) css ZkModalStyles.title

        + column(ZkModalStyles.content) {
            style {
                alignItems = "center"
            }

            + div(ZkFormStyles.sectionSummary) {
                + strings.sessionRenew
            } marginBottom 5

            + LoginForm(
                accountName = executor.account.accountName,
                onCancel = { window.location.href = "/" },
                onSuccess = { io { channel.send(true) } }
            )
        }
    }
}