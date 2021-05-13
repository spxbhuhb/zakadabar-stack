/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.login

import kotlinx.browser.window
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.account.LoginAction
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.fields.ZkConstStringField
import zakadabar.stack.frontend.builtin.toast.dangerToast
import zakadabar.stack.frontend.builtin.toast.warningToast
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.marginBottom

class LoginForm(
    private val accountName: String? = null,
    val onCancel: () -> Unit = { },
    val onSuccess: () -> Unit
) : ZkForm<LoginAction>() {

    override fun onConfigure() {
        dto = default {
            this@LoginForm.accountName?.let { accountName = it }
        }
        mode = ZkElementMode.Action
        fieldGridColumnTemplate = "minmax(max-content, 100px) 1fr"
        onExecuteResult = ::onExecuteResult
    }

    override fun onCreate() {
        super.onCreate()

        style {
            width = "min(100%, 300px)"
        }

        + fieldGrid {
            if (accountName == null) {
                + dto::accountName
            } else {
                + ZkConstStringField(this@LoginForm, stringStore["accountName"], dto.accountName)
            }
            + dto::password
        } marginBottom 20

        + row {
            style {
                width = "100%"
                justifyContent = "space-between"
            }
            + ZkButton(stringStore.cancel) { onCancel() }
            + ZkButton(stringStore.login) { this@LoginForm.submit() }
        }
    }

    override fun onResume() {
        super.onResume()
        window.requestAnimationFrame {
            if (accountName == null) {
                dto::accountName.find().focus()
            } else {
                dto::password.find().focus()
            }
        }
    }

    override fun onInvalidSubmit() {
        invalidToast = warningToast { stringStore.loginFail }
    }

    override fun onSubmitSuccess() {}

    override fun onSubmitError(ex: Exception) {
        invalidToast = dangerToast { stringStore.loginFail }
    }

    private fun onExecuteResult(resultDto: DtoBase) {

        resultDto as ActionStatusDto

        if (! resultDto.success) {
            if (resultDto.reason == "locked") {
                dangerToast { stringStore.loginLocked }
            } else {
                dangerToast { stringStore.loginFail }
            }
            return
        }

        onSuccess()
    }
}