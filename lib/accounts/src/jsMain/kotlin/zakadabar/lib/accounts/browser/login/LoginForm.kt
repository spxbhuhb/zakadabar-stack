/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.login

import kotlinx.browser.window
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.field.ZkConstStringField
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.toast.toastDanger
import zakadabar.core.browser.toast.toastWarning
import zakadabar.core.browser.util.marginBottom
import zakadabar.core.data.ActionStatus
import zakadabar.core.data.BaseBo
import zakadabar.core.exception.Unauthorized
import zakadabar.core.resource.css.JustifyContent
import zakadabar.core.resource.css.percent
import zakadabar.core.resource.localizedStrings
import zakadabar.core.util.default
import zakadabar.lib.accounts.data.LoginAction

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
            + bo::password submitOnEnter true
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
        // TODO remove this when fixed in compiler, when not here onExecuteResult is not executed
        if (resultBo is ActionStatus && resultBo.success) println("login successful")
        onSuccess()
    }
}