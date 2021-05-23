/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.accounts

import kotlinx.coroutines.coroutineScope
import zakadabar.stack.StackRoles
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.builtin.account.*
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.executor
import zakadabar.stack.frontend.application.hasRole
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.crud.ZkCrudEditor
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkSecretField
import zakadabar.stack.frontend.builtin.form.fields.ZkSecretVerificationField
import zakadabar.stack.frontend.builtin.form.structure.ZkFormButtons
import zakadabar.stack.frontend.builtin.input.ZkCheckboxList
import zakadabar.stack.frontend.builtin.input.ZkCheckboxListItem
import zakadabar.stack.frontend.builtin.layout.tabcontainer.ZkTabContainer
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitleProvider
import zakadabar.stack.frontend.builtin.toast.toastDanger
import zakadabar.stack.frontend.builtin.toast.toastSuccess
import zakadabar.stack.frontend.builtin.toast.toastWarning
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

class Form : ZkElement(), ZkCrudEditor<AccountPrivateBo>, ZkAppTitleProvider {

    override lateinit var bo: AccountPrivateBo
    override lateinit var mode: ZkElementMode
    override var openUpdate: ((bo: AccountPrivateBo) -> Unit)? = null
    override var onBack = {  }

    override var setAppTitle = true
    override var addLocalTitle = false
    override var titleText: String? = null
    override var titleElement: ZkAppTitle? = null


    private lateinit var principalBo: PrincipalBo
    private lateinit var systemRoles: List<RoleBo>
    private lateinit var userRoles: List<RoleGrantBo>

    override fun onCreate() {
        super.onCreate()
        io {
            if (hasRole(StackRoles.securityOfficer) && mode != ZkElementMode.Create) {
                coroutineScope {
                    principalBo = PrincipalBo.read(bo.principal)
                    systemRoles = RoleBo.all()
                    RoleGrantBo.Companion.comm // to initialize comm
                    userRoles = RoleGrantsByPrincipal(bo.principal).execute()
                }
            }

            classList += zkLayoutStyles.grow

            titleText = if (mode != ZkElementMode.Create) bo.accountName else stringStore.account

            + AccountTabContainer()
        }
    }

    override fun onResume() {
        super.onResume()
        setAppTitleBar()
    }

    inner class AccountTabContainer : ZkTabContainer() {

        override fun onCreate() {
            super.onCreate()

            // Add a small padding around the container and make it grow.
            // We want this particular container to fill the whole page.

            classList += zkLayoutStyles.grow

            // This is needed to make the content scrollable if there is
            // not enough vertical space for it.

            style {
                minHeight = "0px"
            }

            + tab(stringStore.basics) {
                + BasicDataForm()
            }

            if (mode != ZkElementMode.Create) {

                + tab(stringStore.passwordChange) {
                    + PasswordChangeForm()
                }

                if (hasRole(StackRoles.securityOfficer)) {
                    + tab(stringStore.roles) { + RolesForm() }
                    + tab(stringStore.accountStatus) { + PrincipalForm() }
                }
            }
        }

    }

    inner class BasicDataForm : ZkForm<AccountPrivateBo>() {

        init {
            bo = this@Form.bo
            mode = this@Form.mode
            setAppTitle = false
        }

        override fun onCreate() {
            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildPoint.classList += ZkFormStyles.onePanel
                    + basics()
                    + buttons()
                    + invalidFieldList()
                }
            }
        }

        private fun basics() = section(stringStore.basics) {
            with(bo) {
                + ::id
                + ::accountName
                + ::fullName
                + ::email
                + ::phone
                + ::organizationName
                + ::position
            }
        }
    }

    inner class PasswordChangeForm : ZkForm<PasswordChangeAction>() {

        init {
            bo = default { this.accountId = this@Form.bo.id }
            mode = ZkElementMode.Action
            setAppTitle = false
            onExecuteResult = ::onExecuteResult
        }

        override fun onCreate() {
            PrincipalBo.Companion.comm // initialize comm

            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildPoint.classList += ZkFormStyles.onePanel

                    val expl = if (executor.account.id == bo.accountId) {
                        stringStore.passwordChangeExpOwn
                    } else {
                        stringStore.passwordChangeExpSo
                    }

                    + section(stringStore.passwordChange, expl) {

                        // when the user changes his/her own password ask for the old one
                        // when security officer changes the password of someone else, this
                        // field won't be shown as the account id is different
                        if (executor.account.id == bo.accountId) {
                            + bo::oldPassword
                        } else {
                            bo.oldPassword.value = "*" // so the schema will be valid
                        }

                        + newSecret(bo::newPassword)
                        + ZkSecretVerificationField(this@PasswordChangeForm, bo::newPassword)
                    }

                    + buttons()
                }
            }
        }

        override fun validate(submit: Boolean): Boolean {
            if (! super.validate(submit)) return false

            val newField = fields.find { it.propName == "newPassword" } as ZkSecretField
            val verificationField = get(ZkSecretVerificationField::class)

            if (submit || (newField.touched && verificationField.touched)) {
                if (bo.newPassword.value != verificationField.verificationValue) {
                    verificationField.valid = false
                    return false
                } else {
                    verificationField.valid = true
                }
            }

            return true
        }

        override fun onInvalidSubmit() {
            toastWarning { stringStore.passwordChangeInvalid }
        }

        override fun onSubmitSuccess() {}

        private fun onExecuteResult(resultBo: BaseBo) {
            resultBo as ActionStatusBo

            if (! resultBo.success) {
                toastDanger { stringStore.passwordChangeFail }
            } else {
                toastSuccess { stringStore.actionSuccess }
            }
        }
    }

    inner class PrincipalForm : ZkForm<PrincipalBo>() {

        init {
            bo = this@Form.principalBo
            mode = ZkElementMode.Update
            setAppTitle = false
        }

        override fun onCreate() {
            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildPoint.classList += ZkFormStyles.onePanel

                    + section(stringStore.accountStatus) {
                        + bo::locked
                        + bo::lastLoginSuccess
                        + bo::lastLoginFail
                        + constString(stringStore.loginFailCount) { bo.loginFailCount.toString() }
                    }

                    + buttons()
                }
            }
        }

    }

    inner class RolesForm : ZkForm<RoleGrantBo>() {

        init {
            bo = default { principal = this@Form.principalBo.id }
            mode = ZkElementMode.Other
            setAppTitle = false
        }

        val items = systemRoles.sortedBy { it.description }.map { sr ->
            ZkCheckboxListItem(sr.id, stringStore.getNormalized(sr.description), userRoles.firstOrNull { ur -> ur.role == sr.id } != null)
        }

        override fun onCreate() {
            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildPoint.classList += ZkFormStyles.onePanel

                    + section(stringStore.roles) {
                        + ZkCheckboxList(items)
                    }

                    + ZkFormButtons(this@RolesForm, ::onExecute)
                }
            }
        }

        private fun onExecute() {
            io {
                try {
                    items.forEach {
                        if (it.selected) {
                            grant(it.value)
                        } else {
                            revoke(it.value)
                        }
                    }
                    userRoles = RoleGrantsByPrincipal(bo.principal).execute()
                    onSubmitSuccess()
                } catch (ex: Exception) {
                    onSubmitError(ex)
                }
            }
        }

        private suspend fun grant(roleId: EntityId<RoleBo>) {
            if (userRoles.firstOrNull { it.role == roleId } != null) return
            RoleGrantBo(EntityId(), principalBo.id, roleId).create()
        }

        private suspend fun revoke(roleId: EntityId<RoleBo>) {
            userRoles.forEach {
                if (it.role != roleId) return@forEach
                it.delete()
            }
        }
    }

}