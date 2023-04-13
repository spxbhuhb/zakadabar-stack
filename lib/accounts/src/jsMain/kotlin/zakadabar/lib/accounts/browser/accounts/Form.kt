/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.browser.accounts

import kotlinx.coroutines.coroutineScope
import zakadabar.core.authorize.appRoles
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.ZkElementMode
import zakadabar.core.browser.application.application
import zakadabar.core.browser.application.executor
import zakadabar.core.browser.crud.ZkCrudEditor
import zakadabar.core.browser.field.ZkPropSecretField
import zakadabar.core.browser.field.ZkPropSecretVerificationField
import zakadabar.core.browser.form.ZkForm
import zakadabar.core.browser.form.ZkFormButtons
import zakadabar.core.browser.form.zkFormStyles
import zakadabar.core.browser.input.ZkCheckboxList
import zakadabar.core.browser.input.ZkCheckboxListItem
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.browser.tabcontainer.ZkTabContainer
import zakadabar.core.browser.titlebar.ZkAppTitle
import zakadabar.core.browser.titlebar.ZkAppTitleProvider
import zakadabar.core.browser.toast.toastDanger
import zakadabar.core.browser.toast.toastSuccess
import zakadabar.core.browser.toast.toastWarning
import zakadabar.core.browser.util.io
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.data.EntityId
import zakadabar.core.module.modules
import zakadabar.core.resource.localizedStrings
import zakadabar.core.text.TranslationProvider
import zakadabar.core.util.default
import zakadabar.lib.accounts.data.*

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
    private lateinit var locales: List<Pair<String, String>>

    val defaultLocale
        get() = application.serverDescription.defaultLocale

    val defaultLocaleList
        get() = listOf(defaultLocale to defaultLocale)

    private lateinit var accountState: AccountStateBo

    override fun onCreate() {
        super.onCreate()

        titleText = if (mode == ZkElementMode.Create) localizedStrings.account else bo.accountName

        io {
            locales = modules.firstOrNull<TranslationProvider>()
                ?.getLocales()
                ?.let { it.ifEmpty { defaultLocaleList } }
                ?: defaultLocaleList

            if (mode == ZkElementMode.Create) {

                systemRoles = RoleBo.all()
                userRoles = emptyList()

                + CreateForm()

            } else {

                if (hasRole(appRoles.securityOfficer)) {
                    coroutineScope {
                        accountState = GetAccountState(bo.id).execute()
                        systemRoles = RoleBo.all()
                        userRoles = RolesByAccount(EntityId(bo.id)).execute()
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
            ZkCheckboxListItem(sr.id, localizedStrings.getNormalized(sr.description), userRoles.firstOrNull { ur -> ur.role == sr.id } != null)
        }

        override fun onConfigure() {
            bo = default {
                locale = defaultLocale
            }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            super.onCreate()
            + div(zkFormStyles.contentContainer) {
                + column(zkFormStyles.form) {
                    buildPoint.classList += zkFormStyles.onePanel

                    + basics()

                    + section(localizedStrings.password) {
                        + bo::credentials newSecret true
                        + ZkPropSecretVerificationField(this@CreateForm, bo::credentials).also { fields += it }
                    }

                    + section(localizedStrings.roles) {
                        + ZkCheckboxList(items)
                    }

                    + buttons()
                    + invalidFieldList()
                }
            }
        }

        private fun basics() = section(localizedStrings.basics) {
            with(bo) {
                + ::accountName
                + ::fullName
                + ::email
                + ::phone
                + ::locale.asSelect() options2 { locales }
            }
        }

        override fun validate(submit: Boolean): Boolean {
            if (! submit) {
                // invalidInput is set by the account name duplication checker.
                // if the user edits the field it should turn to false
                bo::accountName.find().invalidInput = false
            }

            if (! super.validate(submit)) return false

            if (bo.accountName.trim() != bo.accountName) {
                bo::accountName.find().valid = false
                // FIXME replace this with a message integrated with the form
                if (submit) toastDanger { localizedStrings.accountTrimSpaces }
                return false
            }

            val passwordField = bo::credentials.find() as ZkPropSecretField
            val verificationField = first<ZkPropSecretVerificationField>()

            if (submit || (passwordField.touched && verificationField.touched)) {
                if (bo.credentials.value != verificationField.verificationValue) {
                    verificationField.valid = false
                    return false
                } else {
                    verificationField.valid = true
                }
            }

            return true
        }

        override suspend fun onSubmitStart() {
            bo.roles = items.mapNotNull { if (it.selected) it.value else null }

            if (bo.accountName.isEmpty()) {
                fields.find { it.propName == "accountName" } !!.invalidInput = true
                // for some reason this does not work, throws an exception :S
                // bo::accountName.find().invalidInput = true
                return
            }

            // TODO make this a real-time check, as the user types
            val cid = CheckName(bo.accountName).execute().accountId
            if (cid != null) {
                toastWarning { localizedStrings.accountNameConflict }
                fields.find { it.propName == "accountName" } !!.invalidInput = true
                // for some reason this does not work, throws an exception :S
                // bo::accountName.find().invalidInput = true
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

            + tab(localizedStrings.basics) {
                + BasicDataForm()
            }

            if (mode != ZkElementMode.Create) {

                + tab(localizedStrings.passwordChange) {
                    + PasswordChangeForm()
                }

                if (hasRole(appRoles.securityOfficer)) {
                    + tab(localizedStrings.roles) { + RolesForm() }
                    + tab(localizedStrings.accountStatus) { + StateForm() }
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
            + div(zkFormStyles.contentContainer) {
                + column(zkFormStyles.form) {
                    buildPoint.classList += zkFormStyles.onePanel
                    + basics()
                    + buttons()
                    + invalidFieldList()
                }
            }
        }

        private fun basics() = section(localizedStrings.basics) {
            with(bo) {
                + ::id
                + ::accountName
                + ::fullName
                + ::email
                + ::phone
                + ::locale.asSelect() options2 { locales }
                + ::uuid readOnly true
            }
        }

        override suspend fun onSubmitStart() {
            // TODO make this a real-time check, as the user types
            val cid = CheckName(bo.accountName).execute().accountId
            if (cid != null && cid != bo.id) {
                toastWarning { localizedStrings.accountNameConflict }
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
            + div(zkFormStyles.contentContainer) {
                + column(zkFormStyles.form) {
                    buildPoint.classList += zkFormStyles.onePanel

                    val expl = if (executor.account.accountId == bo.accountId) {
                        localizedStrings.passwordChangeExpOwn
                    } else {
                        localizedStrings.passwordChangeExpSo
                    }

                    + section(localizedStrings.passwordChange, expl) {

                        // when the user changes his/her own password ask for the old one
                        // when security officer changes the password of someone else, this
                        // field won't be shown as the account id is different
                        if (executor.account.accountId == bo.accountId) {
                            + bo::oldPassword
                        } else {
                            bo.oldPassword.value = "*" // so the schema will be valid
                        }

                        + bo::newPassword newSecret true
                        + ZkPropSecretVerificationField(this@PasswordChangeForm, bo::newPassword).also {
                            fields += it
                        }
                    }

                    + buttons()
                }
            }
        }

        override fun validate(submit: Boolean): Boolean {
            if (! super.validate(submit)) return false

            val verificationField = first<ZkPropSecretVerificationField>()

            if (submit || verificationField.touched) {
                verificationField.valid = (bo.newPassword.value == verificationField.verificationValue)
            }

            if (submit && ! verificationField.valid) {
                onInvalidSubmit()
            }

            return verificationField.valid
        }

        override fun onInvalidSubmit() {
            toastWarning { localizedStrings.passwordChangeInvalid }
        }

        override fun onSubmitSuccess() {
            toastSuccess { localizedStrings.actionSuccess }
        }

        override fun onSubmitError(ex: Exception) {
            toastDanger { localizedStrings.passwordChangeFail }
        }
    }

    inner class StateForm : ZkForm<UpdateAccountLocked>() {

        override fun onConfigure() {
            bo = default {
                accountId = this@Form.bo.id
                locked = this@Form.accountState.locked
            }
            mode = ZkElementMode.Action
            setAppTitle = false
        }

        override fun onCreate() {
            onConfigure()

            + div(zkFormStyles.contentContainer) {
                + column(zkFormStyles.form) {
                    buildPoint.classList += zkFormStyles.onePanel

                    + section(localizedStrings.accountStatus) {
                        + bo::locked
                        with(this@Form.accountState) {
                            + ::validated readOnly true
                            + ::expired readOnly true
                            + ::anonymized readOnly true
                            + ::lastLoginSuccess readOnly true
                            + ::loginSuccessCount readOnly true
                            + ::lastLoginFail readOnly true
                            + ::loginFailCount readOnly true
                        }
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
            ZkCheckboxListItem(sr.id, localizedStrings.getNormalized(sr.description), userRoles.firstOrNull { ur -> ur.role == sr.id } != null)
        }

        override fun onCreate() {
            onConfigure()
            + div(zkFormStyles.contentContainer) {
                + column(zkFormStyles.form) {
                    buildPoint.classList += zkFormStyles.onePanel

                    + section(localizedStrings.roles) {
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
                    userRoles = RolesByAccount(EntityId(bo.id)).execute()
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