/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.login

import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.account.LoginAction
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarStyles
import zakadabar.stack.frontend.builtin.toast.toast
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.marginBottom

class LoginForm(
    val onSuccess: () -> Unit
) : ZkForm<LoginAction>() {

    override fun onConfigure() {
        dto = default { }
        mode = ZkElementMode.Action
        fieldGridColumnTemplate = "minmax(max-content, 100px) 1fr"
        onExecuteResult = ::onExecuteResult
    }

    override fun onCreate() {
        super.onCreate()

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

    private fun onExecuteResult(resultDto: DtoBase) {

        resultDto as ActionStatusDto

        if (! resultDto.success) {
            if (resultDto.reason == "locked") {
                toast(error = true) { strings.loginLocked }
            } else {
                toast(error = true) { strings.loginFail }
            }
            return
        }

        onSuccess()
    }
}