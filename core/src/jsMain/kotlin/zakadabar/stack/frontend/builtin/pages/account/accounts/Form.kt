/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.pages.account.accounts

import kotlinx.coroutines.coroutineScope
import zakadabar.stack.StackRoles
import zakadabar.stack.data.DtoBase
import zakadabar.stack.data.builtin.*
import zakadabar.stack.data.builtin.account.*
import zakadabar.stack.data.record.EmptyRecordId
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.application.ZkApplication.executor
import zakadabar.stack.frontend.application.ZkApplication.hasRole
import zakadabar.stack.frontend.application.ZkApplication.strings
import zakadabar.stack.frontend.application.ZkApplication.t
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementMode
import zakadabar.stack.frontend.builtin.form.ZkForm
import zakadabar.stack.frontend.builtin.form.ZkFormStyles
import zakadabar.stack.frontend.builtin.form.fields.ZkSecretField
import zakadabar.stack.frontend.builtin.form.fields.ZkSecretVerificationField
import zakadabar.stack.frontend.builtin.form.structure.ZkFormButtons
import zakadabar.stack.frontend.builtin.input.ZkCheckboxList
import zakadabar.stack.frontend.builtin.input.ZkCheckboxListItem
import zakadabar.stack.frontend.builtin.layout.tabcontainer.ZkTabContainer
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.builtin.pages.ZkCrudPage
import zakadabar.stack.frontend.builtin.titlebar.ZkAppTitle
import zakadabar.stack.frontend.builtin.toast.toastDanger
import zakadabar.stack.frontend.builtin.toast.toastSuccess
import zakadabar.stack.frontend.builtin.toast.toastWarning
import zakadabar.stack.frontend.util.default
import zakadabar.stack.frontend.util.io
import zakadabar.stack.frontend.util.plusAssign

class Form : ZkElement(), ZkCrudPage<AccountPrivateDto> {

    override lateinit var dto: AccountPrivateDto
    override lateinit var mode: ZkElementMode
    override var openUpdate: ((dto: AccountPrivateDto) -> Unit)? = null

    private lateinit var principalDto: PrincipalDto
    private lateinit var systemRoles: List<RoleDto>
    private lateinit var userRoles: List<RoleGrantDto>

    override fun onCreate() {
        super.onCreate()
        io {
            if (hasRole(StackRoles.securityOfficer) && mode != ZkElementMode.Create) {
                coroutineScope {
                    principalDto = PrincipalDto.read(dto.principal)
                    systemRoles = RoleDto.all()
                    RoleGrantDto.Companion.comm // to initialize comm
                    userRoles = RoleGrantsByPrincipal(dto.principal).execute()
                }
            }

            classList += zkLayoutStyles.grow

            ZkApplication.title = ZkAppTitle(if (mode != ZkElementMode.Create) dto.accountName else strings.account)

            + AccountTabContainer()
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

            + tab(strings.basics) {
                + BasicDataForm()
            }

            if (mode != ZkElementMode.Create) {

                + tab(strings.passwordChange) {
                    + PasswordChangeForm()
                }

                if (hasRole(StackRoles.securityOfficer)) {
                    + tab(strings.roles) { + RolesForm() }
                    + tab(strings.accountStatus) { + PrincipalForm() }
                }
            }
        }

    }

    inner class BasicDataForm : ZkForm<AccountPrivateDto>() {

        init {
            dto = this@Form.dto
            mode = this@Form.mode
            appTitle = false
        }

        override fun onCreate() {
            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildElement.classList += ZkFormStyles.onePanel
                    + basics()
                    + buttons()
                    + invalidFieldList()
                }
            }
        }

        private fun basics() = section(strings.basics) {
            with(dto) {
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
            dto = default { this.accountId = this@Form.dto.id }
            mode = ZkElementMode.Action
            appTitle = false
            onExecuteResult = ::onExecuteResult
        }

        override fun onCreate() {
            PrincipalDto.Companion.comm // initialize comm

            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildElement.classList += ZkFormStyles.onePanel

                    val expl = if (executor.account.id == dto.accountId) {
                        strings.passwordChangeExpOwn
                    } else {
                        strings.passwordChangeExpSo
                    }

                    + section(strings.passwordChange, expl) {

                        // when the user changes his/her own password ask for the old one
                        // when security officer changes the password of someone else, this
                        // field won't be shown as the account id is different
                        if (executor.account.id == dto.accountId) {
                            + dto::oldPassword
                        } else {
                            dto.oldPassword.value = "*" // so the schema will be valid
                        }

                        + newSecret(dto::newPassword)
                        + ZkSecretVerificationField(this@PasswordChangeForm, dto::newPassword)
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
                if (dto.newPassword.value != verificationField.verificationValue) {
                    verificationField.valid = false
                    return false
                } else {
                    verificationField.valid = true
                }
            }

            return true
        }

        override fun onInvalidSubmit() {
            toastWarning { strings.passwordChangeInvalid }
        }

        override fun onSubmitSuccess() {}

        private fun onExecuteResult(resultDto: DtoBase) {
            resultDto as ActionStatusDto

            if (! resultDto.success) {
                toastDanger { strings.passwordChangeFail }
            } else {
                toastSuccess { strings.actionSuccess }
            }
        }
    }

    inner class PrincipalForm : ZkForm<PrincipalDto>() {

        init {
            dto = this@Form.principalDto
            mode = ZkElementMode.Update
            appTitle = false
        }

        override fun onCreate() {
            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildElement.classList += ZkFormStyles.onePanel

                    + section(strings.accountStatus) {
                        + dto::locked
                        + dto::lastLoginSuccess
                        + dto::lastLoginFail
                        + constString(strings.loginFailCount) { dto.loginFailCount.toString() }
                    }

                    + buttons()
                }
            }
        }

    }

    inner class RolesForm : ZkForm<RoleGrantDto>() {

        init {
            dto = default { principal = this@Form.principalDto.id }
            mode = ZkElementMode.Other
            appTitle = false
        }

        val items = systemRoles.sortedBy { it.description }.map { sr ->
            ZkCheckboxListItem(sr.id, t(sr.description), userRoles.firstOrNull { ur -> ur.role == sr.id } != null)
        }

        override fun onCreate() {
            + div(ZkFormStyles.contentContainer) {
                + column(ZkFormStyles.form) {
                    buildElement.classList += ZkFormStyles.onePanel

                    + section(strings.roles) {
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
                    userRoles = RoleGrantsByPrincipal(dto.principal).execute()
                    onSubmitSuccess()
                } catch (ex: Exception) {
                    onSubmitError(ex)
                }
            }
        }

        private suspend fun grant(roleId: RecordId<RoleDto>) {
            if (userRoles.firstOrNull { it.role == roleId } != null) return
            RoleGrantDto(EmptyRecordId(), principalDto.id, roleId).create()
        }

        private suspend fun revoke(roleId: RecordId<RoleDto>) {
            userRoles.forEach {
                if (it.role != roleId) return@forEach
                it.delete()
            }
        }
    }

}