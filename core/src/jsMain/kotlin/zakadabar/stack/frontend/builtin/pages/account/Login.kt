/*
 * Copyright © 2020, Simplexion, Hungary. All rights reserved.
 *
 * This source code contains proprietary information; it is provided under a
 * license agreement containing restrictions on use and distribution and are
 * also protected by copyright, patent, and other intellectual and industrial
 * property laws.
 */
package zakadabar.stack.frontend.builtin.pages.account

import kotlinx.browser.window
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.account.LoginAction
import zakadabar.stack.data.builtin.account.SessionDto
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.application.ZkExecutor
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarStyles
import zakadabar.stack.frontend.builtin.toast.toast
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.marginBottom

object Login : ZkPage(ZkFullScreenLayout) {

    override fun onCreate() {

        + column {

            style {
                alignItems = "center"
                height = "100vh"
                width = "100vw"
            }

            + gap(height = 60)

            + LoginForm()
        }
    }

    class LoginForm : ZkForm<LoginAction>() {

        init {
            dto = default { }
            mode = ZkElementMode.Action
            fieldGridColumnTemplate = "minmax(max-content, 100px) 1fr"
            onExecuteResult = Login::onExecuteResult
        }

        override fun onCreate() {
            style {
                width = "min(100%, 300px)"
            }

            + div(ZkTitleBarStyles.appTitleBar) {
                + strings.applicationName
            } marginBottom 20

            + fieldGrid {
                + dto::accountName
                + dto::password
            } marginBottom 20

            + row {
                style {
                    width = "100%"
                    justifyContent = "flex-end"
                }
                + ZkButton(strings.login) { this@LoginForm.submit() }
            }
        }

        override fun onInvalidSubmit() {
            invalidToast = toast(warning = true) { strings.loginFail }
        }

        override fun onSubmitSuccess() {}

        override fun onSubmitError(ex: Exception) {
            invalidToast = toast(error = true) { strings.loginFail }
        }
    }

    fun onExecuteResult(resultDto: DtoBase) {

        resultDto as ActionStatusDto

        if (! resultDto.success) {
            if (resultDto.reason == "locked") {
                toast(error = true) { strings.loginLocked }
            } else {
                toast(error = true) { strings.loginFail }
            }
            return
        }

        io {
            val session = SessionDto.read(0L)
            ZkApplication.executor = ZkExecutor(session.account, session.anonymous, session.roles)
            window.location.pathname = "/"
        }
    }
}