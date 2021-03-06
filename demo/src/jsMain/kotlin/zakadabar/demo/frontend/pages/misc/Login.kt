/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.demo.frontend.pages.misc

import zakadabar.demo.frontend.resources.Strings
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.LoginAction
import zakadabar.stack.data.builtin.SessionDto
import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.application.Executor
import zakadabar.stack.frontend.builtin.ZkPage
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormMode
import zakadabar.stack.frontend.builtin.layout.ZkFullScreenLayout
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
                password = "demo"
            }
            mode = ZkFormMode.Action
            onExecuteResult = ::onExecuteResult
        }

        override fun onCreate() {
            style {
                width = "min(100%, 300px)"
            }

            + div { + Strings.login } marginBottom 20

            + fieldGrid {
                + dto::accountName
                + dto::password
            } marginBottom 20

            + row {
                style {
                    width = "100%"
                    justifyContent = "space-between"
                }
                + ZkButton(Strings.forgotten) { /* PasswordReset.open() */ }
                + ZkButton(Strings.login) { this@LoginForm.submit() }
            }
        }
    }

    fun onExecuteResult(resultDto: DtoBase) {
        resultDto as ActionStatusDto

        if (resultDto.success) io {
            val session = SessionDto.read(0L)
            Application.executor = Executor(session.account, session.anonymous, session.roles)
            Home.open()
        }

        toast(error = true) { Strings.loginFail }
    }

}