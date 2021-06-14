/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.backend

import io.ktor.features.*
import kotlinx.datetime.Clock
import zakadabar.lib.accounts.data.*
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.*
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.module
import zakadabar.stack.backend.server
import zakadabar.stack.backend.setting.setting
import zakadabar.stack.backend.util.default
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

    private val settings by setting<ModuleSettings>()

    override val pa = AccountPrivateExposedPa()

    private lateinit var anonymous: AccountPublicBo

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
                is CheckName -> authorize(executor, StackRoles.siteMember)
                is CreateAccount -> authorize(executor, StackRoles.securityOfficer)
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
        action(CheckName::class, ::checkName)
        action(CreateAccount::class, ::createAccount)
        action(PasswordChange::class, ::passwordChange)
        action(UpdateAccountSecure::class, ::updateAccountSecure)
        action(UpdateAccountLocked::class, ::updateAccountLocked)
    }

    override val auditor = auditor {
        includeData = false
    }

    override fun onModuleStart() {
        initDb()
        anonymous = pa.withTransaction {
            pa.readByName("anonymous").toPublic()
        }
        super.onModuleStart() // this has to be last, so authorizer will find roles after db init
    }

    override fun create(executor: Executor, bo: AccountPrivateBo): AccountPrivateBo {
        throw NotImplementedError("use CreateAccount action instead")
    }

    open fun checkName(executor: Executor, action: CheckName): CheckNameResult {

        return try {
            CheckNameResult(
                action.accountName,
                accountId = EntityId(pa.readByName(action.accountName).id)
            )
        } catch (ex: NoSuchElementException) {
            CheckNameResult(
                action.accountName,
                accountId = null
            )
        }

    }

    open fun createAccount(executor: Executor, action: CreateAccount): ActionStatusBo {

        try {
            pa.readByName(action.accountName)
            throw BadRequestException("account with the same account name already exists")
        } catch (ex: NoSuchElementException) {
            // this is fine, this is what we want
        }

        val account = default<AccountPrivateBo> {
            validated = true
            credentials = action.credentials
            accountName = action.accountName
            fullName = action.fullName
            email = action.email
            phone = action.phone
            locale = action.locale
        }

        pa.create(account)

        action.roles.forEach {
            roleBl.grantRole(executor, GrantRole(account.id, it))
        }

        return ActionStatusBo()
    }


    /**
     * Updates only the non-security fields of the account. Security related fields have
     * their own update methods. Email and password are security related fields.
     */
    override fun update(executor: Executor, bo: AccountPrivateBo): AccountPrivateBo {
        val account = pa.read(bo.id)
        val credentials = pa.readCredentials(account.id)

        account.accountName = bo.accountName
        account.fullName = bo.fullName
        account.displayName = bo.displayName
        account.theme = bo.theme
        account.locale = bo.locale

        pa.update(account)
        pa.writeCredentials(account.id, credentials)

        return account
    }

    open fun updateAccountSecure(executor: Executor, action: UpdateAccountSecure): ActionStatusBo {

        val account = pa.read(action.accountId)
        val credentials = pa.readCredentials(account.id)

        account.email = action.email
        account.phone = action.phone

        pa.update(account)
        pa.writeCredentials(account.id, credentials)

        return ActionStatusBo()
    }

    open fun updateAccountLocked(executor: Executor, action: UpdateAccountLocked): ActionStatusBo {

        val account = pa.read(action.accountId)
        val credentials = pa.readCredentials(account.id)

        if (account.locked && ! action.locked) {
            account.loginFailCount = 0
        }

        account.locked = action.locked

        pa.update(account)
        pa.writeCredentials(account.id, credentials)

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
     * to the result. Locks the account if login fails surpass [ModuleSettings.maxFailedLogins].
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

        val credentials = pa.readCredentials(accountId) ?: throw NoSuchElementException()

        val result = when {
            ! account.validated -> AccountNotValidatedException()
            account.locked -> AccountLockedException()
            account.expired -> AccountExpiredException()
            ! BCrypt.checkpw(password, credentials) -> InvalidCredentials()
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

    private fun AccountPrivateBo.toPublic() = AccountPublicBo(
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

    override fun authenticate(executor: Executor, accountName: String, password: Secret): AccountPublicBo {
        val account = pa.readByName(accountName)
        authenticate(executor, account.id, password.value)
        return account.toPublic()
    }

    override fun roles(accountId: EntityId<out BaseBo>): List<Pair<EntityId<out BaseBo>, String>> {
        return roleBl.rolesOf(EntityId(accountId))
    }

    private fun initDb() {

        try {
            pa.withTransaction { pa.readByName("so") }
            return // when there is a security officer, the db is already initialized
        } catch (ex: NoSuchElementException) {
            // this is fine, we have to perform the initialization
        }

        val so: AccountPrivateBo = default {
            validated = true
            locked = settings.initialSoPassword.isNullOrEmpty()
            credentials = settings.initialSoPassword?.let { Secret(it) }
            accountName = "so"
            fullName = "Security Officer"
            email = "so@127.0.0.1"
            displayName = "SO"
            locale = server.settings.defaultLocale
        }

        val anonymous: AccountPrivateBo = default {
            validated = true
            locked = true
            accountName = "anonymous"
            fullName = "Anonymous"
            email = "anonymous@127.0.0.1"
            displayName = "Anonymous"
            locale = server.settings.defaultLocale
        }

        val securityOfficerRole = RoleBo(EntityId(), StackRoles.securityOfficer, "Security Officer")
        val siteAdminRole = RoleBo(EntityId(), StackRoles.siteAdmin, "Site Admin")
        val siteMemberRole = RoleBo(EntityId(), StackRoles.siteMember, "Site Member")

        pa.withTransaction {
            pa.create(so)
            pa.create(anonymous)

            val executor = Executor(so.id, false, emptyList(), emptyList())

            auditor.auditCreate(executor, so)
            auditor.auditCreate(executor, anonymous)

            roleBl.create(executor, securityOfficerRole)
            roleBl.auditor.auditCreate(executor, securityOfficerRole)

            roleBl.create(executor, siteAdminRole)
            roleBl.auditor.auditCreate(executor, siteAdminRole)

            roleBl.create(executor, siteMemberRole)
            roleBl.auditor.auditCreate(executor, siteMemberRole)

            fun grant(role: RoleBo) {
                val grant = GrantRole(so.id, role.id)
                roleBl.grantRole(executor, grant)
                roleBl.auditor.auditAction(executor, grant)
            }

            grant(securityOfficerRole)
            grant(siteAdminRole)
            grant(siteMemberRole)
        }
    }


}