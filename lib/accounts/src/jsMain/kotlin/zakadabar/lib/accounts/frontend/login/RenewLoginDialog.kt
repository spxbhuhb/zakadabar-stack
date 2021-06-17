/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.login

import kotlinx.browser.window
import kotlinx.coroutines.channels.Channel
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.application.executor
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.modal.zkModalStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkLocalTitleBar
import zakadabar.stack.frontend.resources.css.AlignItems
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.resources.localizedStrings

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
        application.modals.show()
        application.modals += this

        channel.receive()

        application.modals -= this
        application.modals.hide()
    }

    override fun onCreate() {
        classList += zkModalStyles.modal

        + ZkLocalTitleBar(localizedStrings.applicationName) css zkModalStyles.title

        + column(zkModalStyles.content) {
            + AlignItems.center

            + div(ZkFormStyles.sectionSummary) {
                + localizedStrings.sessionRenew
            } marginBottom 5

            + LoginForm(
                accountName = executor.account.accountName,
                onCancel = { window.location.href = "/" },
                onSuccess = { io { channel.send(true) } }
            )
        }
    }
}