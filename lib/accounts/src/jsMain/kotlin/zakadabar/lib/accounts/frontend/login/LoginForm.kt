/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.login

import kotlinx.browser.window
import zakadabar.lib.accounts.data.LoginAction
import zakadabar.core.data.BaseBo
import zakadabar.core.exception.Unauthorized
import zakadabar.core.frontend.builtin.ZkElementMode
import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.builtin.form.ZkForm
import zakadabar.core.frontend.builtin.form.fields.ZkConstStringField
import zakadabar.core.frontend.builtin.toast.toastDanger
import zakadabar.core.frontend.builtin.toast.toastWarning
import zakadabar.core.frontend.resources.css.JustifyContent
import zakadabar.core.frontend.resources.css.percent
import zakadabar.core.frontend.util.default
import zakadabar.core.frontend.util.marginBottom
import zakadabar.core.resource.localizedStrings

class LoginForm(
    private val accountName: String? = null,
    val onCancel: (() -> Unit)? = null,
    val onSuccess: () -> Unit
) : ZkForm<LoginAction>() {

    override fun onConfigure() {
        bo = default {
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
                + bo::accountName
            } else {
                + ZkConstStringField(this@LoginForm, localizedStrings.account, bo.accountName)
            }
            + bo::password
        } marginBottom 20

        + row {
            + JustifyContent.spaceBetween
            width = 100.percent

            if (onCancel != null) {
                + ZkButton(localizedStrings.cancel) { onCancel.invoke() }
            } else {
                + div { }
            }
            + ZkButton(localizedStrings.login) { this@LoginForm.submit() }
        }
    }

    override fun onResume() {
        super.onResume()
        window.requestAnimationFrame {
            if (accountName == null) {
                bo::accountName.find().focus()
            } else {
                bo::password.find().focus()
            }
        }
    }

    override fun onInvalidSubmit() {
        invalidToast = toastWarning { localizedStrings.loginFail }
    }

    override fun onSubmitSuccess() {}

    override fun onSubmitError(ex: Exception) {
        invalidToast = when {
            ex !is Unauthorized -> toastDanger { localizedStrings.loginFail }
            ex.data.locked -> toastDanger { localizedStrings.loginLocked }
            ex.data.missingRole -> toastDanger { localizedStrings.loginMissingRole }
            else -> toastDanger { localizedStrings.loginFail }
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onExecuteResult(resultBo: BaseBo) {
        onSuccess()
    }
}