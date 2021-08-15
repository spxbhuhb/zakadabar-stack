/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser

import kotlinx.browser.window
import kotlinx.coroutines.channels.Channel
import zakadabar.lib.accounts.data.LogoutAction
import zakadabar.lib.accounts.data.SessionBo
import zakadabar.lib.accounts.browser.login.RenewLoginDialog
import zakadabar.core.data.EntityId
import zakadabar.core.browser.application.ZkExecutor
import zakadabar.core.browser.application.ZkSessionManager
import zakadabar.core.browser.application.application
import zakadabar.core.browser.modal.ZkMessageDialog
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.log
import zakadabar.core.resource.localizedStrings

/**
 * Handles session renewal.
 */
class SessionManager : ZkSessionManager {

    private val renewChannel = Channel<Channel<Boolean>>()

    /**
     * Initializes the [SessionManager]:
     *
     * * get the session from the server
     * * init `executor` of the ZkApplication from the session
     * * start a renew background task which will perform login renewal if necessary
     *
     */
    override suspend fun init() {
        io { renewTask() }
        val session = SessionBo.read(EntityId("current"))
        application.executor = ZkExecutor(session.account, session.anonymous, session.roles)
        application.serverDescription = session.serverDescription
    }

    /**
     * When the server responds with login timeout status comm instances call
     * [renew] to ask for an authentication renewal. This asks the password
     * of the user in a modal dialog and performs login for the new session.
     *
     * This is a complex process as there may be many simultaneous requests
     * that receive login timeout but there should be only one renewal.
     */
    override suspend fun renew() {
        val responseChannel = Channel<Boolean>()
        renewChannel.send(responseChannel)
        responseChannel.receive()
    }

    /**
     * A background task that performs session renewal when necessary.
     */
    private suspend fun renewTask() {
        for (responseChannel in renewChannel) {
            try {

                // When executor is not initialized the user refreshed the page
                // with an expired session. For now it is fine to redirect him
                // to the home page. It would be possible to save some information
                // into sessionStorage or localStorage but that needs some thinking
                // as it has security and multi-window impact.

                if (! application::executor.isInitialized) {
                    window.location.href = "/"
                    return
                }

                // We don't need to renew anonymous.

                if (application.executor.anonymous) {
                    responseChannel.send(true)
                    continue
                }

                // This is the latest session info from the server.

                var session = SessionBo.read(EntityId("own"))

                // When the id is the same we are OK. In this case we have to go on with the
                // original account information to keep the consistency of the UI. This happens
                // when more requests receive login timeout response.

                if (session.account.accountId == application.executor.account.accountId) {
                    responseChannel.send(true)
                    continue
                }

                // The account of the latest session and the one we would like to use is different.
                // Most probably the latest is anonymous, but that's hopefully not important.
                // We have to ask the user to log in again.

                RenewLoginDialog().run()

                // At this point the should have have a proper session.

                session = SessionBo.read(EntityId("current"))

                if (session.account.accountId == application.executor.account.accountId) {
                    responseChannel.send(true)
                    continue
                }

                // If we reach this point something really strange happened. Better to show
                // a message to the user, logout and then refresh the page.

                ZkMessageDialog(
                    title = localizedStrings.actionFail,
                    message = localizedStrings.sessionRenewError
                ).run()

                LogoutAction().execute()
                window.location.href = "/"

            } catch (ex: Exception) {
                log(ex)
            }
        }
    }
}