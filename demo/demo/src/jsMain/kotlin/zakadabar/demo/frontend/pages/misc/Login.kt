/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.misc

import kotlinx.browser.window
import zakadabar.demo.resources.Strings
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.account.LoginAction
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.builtin.account.SessionDto
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkExecutor
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
import zakadabar.stack.frontend.builtin.pages.ZkPage
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

        // This is here, so the we login form is initialized with
        // the username and password. For [ZkCrud] based forms this
        // is not necessary as ZkCrud creates a default empty instance
        // before creating the form.

        init {
            dto = default {
                accountName = "demo"
                password = Secret("demo")
            }
            mode = ZkElementMode.Action
            fieldGridColumnTemplate = "minmax(max-content, 100px) 1fr"
            onExecuteResult = ::onExecuteResult
        }

        override fun onCreate() {
            style {
                width = "min(100%, 300px)"
            }

            + titleBar(Strings.login) marginBottom 20

            + fieldGrid {
                + dto::accountName
                + dto::password
            } marginBottom 20

            + row {
                style {
                    width = "100%"
                    justifyContent = "space-between"
                }
                + ZkButton(Strings.login) { this@LoginForm.submit() }
            }
        }

        override fun onInvalidSubmit() {
            invalidToast = toast(warning = true) { Strings.loginFail }
        }

        override fun onSubmitSuccess() {}

        override fun onSubmitError(ex: Exception) {
            invalidToast = toast(error = true) { Strings.loginFail }
        }
    }

    fun onExecuteResult(resultDto: DtoBase) {
        resultDto as ActionStatusDto

        if (! resultDto.success) {
            if (resultDto.reason == "locked") {
                toast(error = true) { Strings.loginLocked }
            } else {
                toast(error = true) { Strings.loginFail }
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