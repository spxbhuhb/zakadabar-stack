/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.frontend.accounts

import kotlinx.coroutines.coroutineScope
import zakadabar.lib.accounts.data.*
import zakadabar.stack.StackRoles
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.application.executor
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.crud.ZkCrudEditor
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkOptSecretField
import zakadabar.stack.frontend.builtin.form.fields.ZkOptSecretVerificationField
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
    override var onBack = { }

    override var setAppTitle = true
    override var addLocalTitle = false
    override var titleText: String? = null
    override var titleElement: ZkAppTitle? = null

    private lateinit var systemRoles: List<RoleBo>
    private lateinit var userRoles: List<RoleGrantBo>

    override fun onCreate() {
        super.onCreate()

        titleText = if (mode == ZkElementMode.Create) stringStore.account else bo.accountName


        io {
            if (mode == ZkElementMode.Create) {

                systemRoles = RoleBo.all()
                userRoles = emptyList()

                + CreateForm()

            } else {

                if (hasRole(StackRoles.securityOfficer)) {
                    coroutineScope {
                        systemRoles = RoleBo.all()
                        userRoles = RolesByAccount(bo.id).execute()
                    }
                }

                classList += zkLayoutStyles.grow

                + AccountTabContainer()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        setAppTitleBar()
    }

    inner class CreateForm : ZkForm<CreateAccount>() {

        private val items = systemRoles.sortedBy { it.description }.map { sr ->
            ZkCheckboxListItem(sr.id, stringStore.getNormalized(sr.description), userRoles.firstOrNull { ur -> ur.role == sr.id } != null)
        }

        override fun onConfigure() {
            bo = default { }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            super.onCreate()
            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildPoint.classList += ZkFormStyles.onePanel

                    + basics()

                    + section(stringStore.password) {
                        + bo::credentials newSecret true
                        + ZkOptSecretVerificationField(this@CreateForm, bo::credentials).also { fields += it }
                    }

                    + section(stringStore.roles) {
                        + ZkCheckboxList(items)
                    }

                    + buttons()
                    + invalidFieldList()
                }
            }
        }

        private fun basics() = section(stringStore.basics) {
            with(bo) {
                + ::accountName
                + ::fullName
                + ::email
                + ::phone
            }
        }

        override fun validate(submit: Boolean): Boolean {
            if (! super.validate(submit)) return false

            if (bo.accountName.trim() != bo.accountName) {
                bo::accountName.find().valid = false
                // FIXME replace this with a message integrated with the form
                if (submit) toastDanger { stringStore.accountTrimSpaces }
                return false
            }

            val passwordField = bo::credentials.find() as ZkOptSecretField
            val verificationField = first<ZkOptSecretVerificationField<*>>()

            if (submit || (passwordField.touched && verificationField.touched)) {
                if (bo.credentials?.value != verificationField.verificationValue) {
                    verificationField.valid = false
                    return false
                } else {
                    verificationField.valid = true
                }
            }

            return true
        }

        override suspend fun onSubmitStart() {
            // TODO replace this with a select of locales
            bo.locale = application.serverDescription.defaultLocale
            bo.roles = items.mapNotNull { if (it.selected) it.value else null }

            // TODO make this a real-time check, as the user types
            val cid = CheckName(bo.accountName).execute().accountId
            if (cid != null) {
                toastWarning { stringStore.accountNameConflict }
                bo::accountName.find().invalidInput = true
            }
        }
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

        override fun onConfigure() {
            bo = this@Form.bo
            mode = this@Form.mode
            setAppTitle = false
        }

        override fun onCreate() {
            onConfigure()
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
            }
        }

        override suspend fun onSubmitStart() {
            // TODO make this a real-time check, as the user types
            val cid = CheckName(bo.accountName).execute().accountId
            if (cid != null && cid != bo.id) {
                toastWarning { stringStore.accountNameConflict }
                bo::accountName.find().invalidInput = true
            }
        }
    }

    inner class PasswordChangeForm : ZkForm<PasswordChange>() {

        override fun onConfigure() {
            bo = default { this.accountId = this@Form.bo.id }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            onConfigure()
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

                        + bo::newPassword newSecret true
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

        override fun onSubmitSuccess() {
            toastSuccess { stringStore.actionSuccess }
        }

        override fun onSubmitError(ex: Exception) {
            toastDanger { stringStore.passwordChangeFail }
        }
    }

    inner class PrincipalForm : ZkForm<UpdateAccountLocked>() {

        override fun onConfigure() {
            bo = default {
                accountId = this@Form.bo.id
                locked = this@Form.bo.locked
            }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            onConfigure()

            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildPoint.classList += ZkFormStyles.onePanel

                    + section(stringStore.accountStatus) {
                        + bo::locked
                        + this@Form.bo::lastLoginSuccess readOnly true
                        + this@Form.bo::lastLoginFail readOnly true
                        + this@Form.bo::loginFailCount readOnly true
                    }

                    + buttons()
                }
            }
        }

    }

    inner class RolesForm : ZkForm<AccountPrivateBo>() {

        override fun onConfigure() {
            bo = this@Form.bo
            mode = ZkElementMode.Other
            setAppTitle = false
        }

        private val items = systemRoles.sortedBy { it.description }.map { sr ->
            ZkCheckboxListItem(sr.id, stringStore.getNormalized(sr.description), userRoles.firstOrNull { ur -> ur.role == sr.id } != null)
        }

        override fun onCreate() {
            onConfigure()
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
                    userRoles = RolesByAccount(bo.id).execute()
                    onSubmitSuccess()
                } catch (ex: Exception) {
                    onSubmitError(ex)
                }
            }
        }

        private suspend fun grant(roleId: EntityId<RoleBo>) {
            if (userRoles.firstOrNull { it.role == roleId } != null) return
            GrantRole(bo.id, roleId).execute()
        }

        private suspend fun revoke(roleId: EntityId<RoleBo>) {
            userRoles.forEach {
                if (it.role != roleId) return@forEach
                RevokeRole(bo.id, roleId).execute()
            }
        }
    }

}