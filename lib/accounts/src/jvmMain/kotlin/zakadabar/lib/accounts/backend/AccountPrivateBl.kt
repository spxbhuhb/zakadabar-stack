/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import kotlinx.datetime.Clock
import zakadabar.lib.accounts.data.*
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.*
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.data.builtin.resources.setting
import zakadabar.stack.backend.module
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.BCrypt

open class AccountPrivateBl : EntityBusinessLogicBase<AccountPrivateBo>(
    boClass = AccountPrivateBo::class
), AccountBlProvider {

    private val settings by setting<ModuleSettingsBo>("zakadabar.lib.accounts")

    override val pa = AccountPrivateExposedPa()

    private lateinit var anonymous : AccountPublicBo

    private val roleBl by module<RoleBl>()

    override val authorizer = object : SimpleRoleAuthorizer<AccountPrivateBo>({
        all = StackRoles.securityOfficer // for non-overridden methods
    }) {

        override fun authorizeRead(executor: Executor, entityId: EntityId<AccountPrivateBo>) {
            ownOrSecurityOfficer(executor, entityId)
        }

        override fun authorizeUpdate(executor: Executor, entity: AccountPrivateBo) {
            ownOrSecurityOfficer(executor, entity.id)
        }

        override fun authorizeAction(executor: Executor, actionBo: ActionBo<*>) {
            when (actionBo) {
                is UpdateAccountLocked -> authorize(executor, StackRoles.securityOfficer)
                is PasswordChange -> secureChange(executor, actionBo.accountId, actionBo.oldPassword)
                is UpdateAccountSecure -> secureChange(executor, actionBo.accountId, actionBo.password)
                else -> throw Forbidden()
            }
        }

        /**
         * Users can read and update the non-secure fields their own account, if they are not "anonymous".
         * Security officer can update non-secure fields of all users.
         */
        fun ownOrSecurityOfficer(executor: Executor, accountId: EntityId<AccountPrivateBo>) {
            if (executor.accountId == anonymous.id) throw Forbidden()
            if (executor.accountId == accountId || executor.hasRole(StackRoles.securityOfficer)) {
                return
            } else {
                throw Forbidden()
            }
        }

        /**
         * For changing security related fields (password, e-mail, phone). Security officer can
         * change anything without password, except own.
         */
        fun secureChange(executor: Executor, accountId: EntityId<AccountPrivateBo>, password: Secret) {
            if (executor.accountId == accountId) {
                authenticate(executor, accountId, password.value)
            } else {
                authorize(executor, StackRoles.securityOfficer)
            }
        }
    }

    override val router = router {
        action(PasswordChange::class, ::passwordChange)
        action(UpdateAccountSecure::class, ::updateAccountSecure)
        action(UpdateAccountLocked::class, ::updateAccountLocked)
    }

    override val auditor = auditor {
        includeData = false
    }

    override fun onModuleStart() {
        super.onModuleStart()
        anonymous = pa.withTransaction {
            pa.readByName("anonymous").toPublic()
        }
    }

    override fun create(executor: Executor, bo: AccountPrivateBo): AccountPrivateBo {
        throw NotImplementedError("use CreateAccount action instead")
    }

    /**
     * Updates only the non-security fields of the account. Security related fields have
     * their own update methods. Email and password are security related fields.
     */
    override fun update(executor: Executor, bo: AccountPrivateBo): AccountPrivateBo {
        val account = pa.read(bo.id)

        account.accountName = bo.accountName
        account.fullName = bo.fullName
        account.displayName = bo.displayName
        account.theme = bo.theme
        account.locale = bo.locale

        return pa.update(account)
    }

    open fun updateAccountSecure(executor: Executor, action: UpdateAccountSecure): ActionStatusBo {

        val account = pa.read(action.accountId)

        account.email = action.email
        account.phone = action.phone

        pa.update(account)

        return ActionStatusBo()
    }

    open fun updateAccountLocked(executor: Executor, action: UpdateAccountLocked): ActionStatusBo {

        val account = pa.read(action.accountId)

        if (account.locked && ! action.locked) {
            account.loginFailCount = 0
        }

        account.locked = action.locked

        pa.update(account)

        return ActionStatusBo()
    }

    open fun passwordChange(executor: Executor, action: PasswordChange): ActionStatusBo {

        val bo = pa.read(action.accountId)

        if (executor.accountId == action.accountId) {
            try {
                authenticate(executor, bo.id, action.oldPassword.value)
            } catch (ex: Exception) {
                return ActionStatusBo(false)
            }
        } else {
            authorize(executor, StackRoles.securityOfficer)
        }

        bo.credentials = action.newPassword

        pa.update(bo)

        auditor.auditCustom(executor) { "password change accountId=${bo.id} accountName=${bo.accountName}" }

        return ActionStatusBo()
    }

    /**
     * Perform password based authentication. Increments success/fail counters according
     * to the result. Locks the account if login fails surpass [ModuleSettingsBo.maxFailedLogins].
     *
     * @param   executor   The executor of the authentication.
     * @param   accountId  The account id to authenticate.
     * @param   password   The password to authenticate with.
     *
     * @throws  NoSuchElementException        When the account does not exists.
     * @throws  AccountNotValidatedException  When [AccountPrivateBo.validated] is false.
     * @throws  AccountLockedException        When [AccountPrivateBo.locale] is true.
     * @throws  AccountExpiredException       When [AccountPrivateBo.expired] is true.
     * @throws  InvalidCredentials            When the supplied password is invalid.
     */
    open fun authenticate(executor: Executor, accountId: EntityId<AccountPrivateBo>, password: String) {

        val account = pa.read(accountId)

        val credentials = account.credentials ?: throw NoSuchElementException()

        val result = when {
            ! account.validated -> AccountNotValidatedException()
            account.locked -> AccountLockedException()
            account.expired -> AccountExpiredException()
            ! BCrypt.checkpw(password, credentials.value) -> InvalidCredentials()
            else -> null
        }

        if (result != null) {
            account.loginFailCount ++
            account.lastLoginFail = Clock.System.now()
            account.locked = account.locked || (account.loginFailCount > settings.maxFailedLogins)
            pa.commit()
            auditor.auditCustom(executor) { "login fail accountId=${account.id} accountName=${account.accountName}" }
            throw result
        }

        account.lastLoginSuccess = Clock.System.now()
        account.loginSuccessCount ++

        account.loginFailCount = 0

        pa.commit()
    }

    private fun AccountPrivateBo.toPublic() =  AccountPublicBo(
        id = EntityId(id),
        accountName = accountName,
        fullName = fullName,
        email = email,
        displayName = displayName,
        organizationName = null,
        theme = theme,
        locale = locale
    )
    
    override fun anonymous() = anonymous

    override fun readPublic(account: EntityId<out BaseBo>) = pa.read(EntityId(account)).toPublic()

    override fun authenticate(executor : Executor, accountName: String, password: Secret): AccountPublicBo {
        val account = pa.readByName(accountName)
        authenticate(executor, account.id, password.value)
        return account.toPublic()
    }

    override fun roles(accountId: EntityId<out BaseBo>): List<Pair<EntityId<out BaseBo>, String>> {
        return roleBl.rolesOf(EntityId(accountId))
    }

}