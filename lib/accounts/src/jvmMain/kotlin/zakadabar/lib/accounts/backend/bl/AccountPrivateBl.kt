/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.backend.bl

import io.ktor.features.*
import kotlinx.datetime.Clock
import zakadabar.lib.accounts.backend.pa.AccountCredentialsExposedPa
import zakadabar.lib.accounts.backend.pa.AccountPrivateExposedPa
import zakadabar.lib.accounts.backend.pa.AccountStateExposedPa
import zakadabar.lib.accounts.data.*
import zakadabar.stack.authorize.appRoles
import zakadabar.stack.backend.authorize.AccountBlProvider
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.authorize.authorize
import zakadabar.stack.backend.business.EntityBusinessLogicBase
import zakadabar.stack.backend.server
import zakadabar.stack.backend.setting.setting
import zakadabar.stack.backend.util.default
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.builtin.misc.Secret
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.exceptions.Unauthorized
import zakadabar.stack.exceptions.UnauthorizedData
import zakadabar.stack.module.module
import zakadabar.stack.util.BCrypt

open class AccountPrivateBl : EntityBusinessLogicBase<AccountPrivateBo>(
    boClass = AccountPrivateBo::class,
), AccountBlProvider {

    open val settings by setting<ModuleSettings>()

    override val pa = AccountPrivateExposedPa()
    open val statePa = AccountStateExposedPa()
    open val credentialsPa = AccountCredentialsExposedPa()

    private lateinit var anonymous: AccountPublicBo

    private val roleBl by module<RoleBl>()

    @Suppress("LeakingThis") // this is fine here as authorizer not called during init
    override val authorizer = AccountPrivateBlAuthorizer(this)

    override val router = router {
        query(CheckName::class, ::checkName)
        query(GetAccountState::class, ::getAccountState)
        query(AccountListSecure::class, ::accountListSecure)
        query(AccountList::class, ::accountList)
        action(CreateAccount::class, ::createAccount)
        action(PasswordChange::class, ::passwordChange)
        action(UpdateAccountSecure::class, ::updateAccountSecure)
        action(UpdateAccountLocked::class, ::updateAccountLocked)
    }

    override val auditor = auditor {
        includeData = false
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    override fun onModuleLoad() {
        super.onModuleLoad()
        statePa.onModuleLoad()
        credentialsPa.onModuleLoad()
    }

    override fun onInitializeDb() {

        val so = pa.withTransaction {
            try {
                pa.readByName("so")
            } catch (ex: NoSuchElementException) {
                create(
                    default {
                        validated = true
                        locked = settings.initialSoPassword == null
                        credentials = settings.initialSoPassword?.let { Secret(it) }
                        accountName = "so"
                        fullName = "Security Officer"
                        email = "so@127.0.0.1"
                        locale = server.settings.defaultLocale
                    }
                )
            }
        }

        val anonymous = pa.withTransaction {
            try {
                pa.readByName("anonymous")
            } catch (ex: NoSuchElementException) {
                create(
                    default {
                        validated = true
                        locked = true
                        accountName = "anonymous"
                        fullName = "Anonymous"
                        email = "anonymous@127.0.0.1"
                        locale = server.settings.defaultLocale
                    }
                )
            }
        }

        pa.withTransaction {

            val executor = Executor(so.id, false, emptyList(), emptyList())

            auditor.auditCreate(executor, so)
            auditor.auditCreate(executor, anonymous)

            fun grant(account: AccountPrivateBo, role: RoleBo) {
                val grant = GrantRole(account.id, role.id)
                roleBl.grantRole(executor, grant)
                roleBl.auditor.auditAction(executor, grant)
            }

            appRoles.map.forEach {
                val roleName = it.value

                try {
                    roleBl.getByName(roleName)
                    return@forEach // when exists we don't want to re-create it
                } catch (ex: NoSuchElementException) {
                    // this is fine, we have to create the role
                }

                val bo = roleBl.create(executor, RoleBo(EntityId(), roleName, roleName))
                roleBl.auditor.auditCreate(executor, bo)

                if (it.value == appRoles.securityOfficer) {
                    grant(so, bo)
                }
            }
        }
    }


    override fun onModuleStart() {
        anonymous = pa.withTransaction {
            pa.readByName("anonymous").toPublic()
        }
        super.onModuleStart() // this has to be last, so authorizer will find roles after db init
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    open fun checkName(executor: Executor, query: CheckName): CheckNameResult {

        return try {
            CheckNameResult(
                accountId = EntityId(pa.readByName(query.accountName).id)
            )
        } catch (ex: NoSuchElementException) {
            CheckNameResult(
                accountId = null
            )
        }

    }

    open fun getAccountState(executor: Executor, query: GetAccountState): AccountStateBo =
        statePa.read(query.accountId)

    open fun accountListSecure(executor: Executor, query: AccountListSecure): List<AccountListSecureEntry> =
        pa.accountListSecure(statePa.table)

    open fun accountList(executor: Executor, query: AccountList): List<AccountPublicBo> =
        pa.accountList(
            withEmail = settings.emailInAccountPublic,
            withPhone = settings.phoneInAccountPublic
        )

    // -------------------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------------------

    open fun createAccount(executor: Executor, action: CreateAccount): ActionStatusBo {

        try {
            pa.readByName(action.accountName)
            throw BadRequestException("account with the same account name already exists")
        } catch (ex: NoSuchElementException) {
            // this is fine, this is what we want
        }

        val accountPrivate = create(action)

        action.roles.forEach {
            roleBl.grantRole(executor, GrantRole(accountPrivate.id, it))
        }

        return ActionStatusBo()
    }

    /**
     * Creates private, state, credentials from a [CreateAccount] action BO.
     */
    fun create(action: CreateAccount): AccountPrivateBo {

        val accountPrivate = pa.create(
            default {
                accountName = action.accountName
                fullName = action.fullName
                email = action.email
                phone = action.phone
                locale = action.locale
            }
        )

        statePa.create(
            default {
                accountId = accountPrivate.id
                validated = settings.autoValidate
            }
        )

        action.credentials?.let {
            credentialsPa.create(
                default {
                    accountId = accountPrivate.id
                    type = CredentialTypes.password
                    value = it
                }
            )
        }

        return accountPrivate
    }

    open fun updateAccountSecure(executor: Executor, action: UpdateAccountSecure): ActionStatusBo {

        val account = pa.read(action.accountId)

        account.email = action.email
        account.phone = action.phone

        pa.update(account)

        return ActionStatusBo()
    }

    open fun updateAccountLocked(executor: Executor, action: UpdateAccountLocked): ActionStatusBo {

        val state = statePa.read(action.accountId)

        if (state.locked && ! action.locked) {
            state.loginFailCount = 0
        }

        state.locked = action.locked

        statePa.update(state)

        return ActionStatusBo()
    }

    open fun passwordChange(executor: Executor, action: PasswordChange): ActionStatusBo {

        if (executor.accountId == action.accountId) {
            try {
                authenticate(executor, action.accountId, action.oldPassword.value)
            } catch (ex: Exception) {
                return ActionStatusBo(false)
            }
        } else {
            authorize(executor, appRoles.securityOfficer)
        }

        credentialsPa
            .readOrNull(action.accountId, CredentialTypes.password)
            ?.apply {
                value = action.newPassword
            }
            ?.also {
                credentialsPa.update(it)
            }
            ?: run {
                if (executor.accountId == action.accountId) throw Unauthorized("NoPasswordRecord")

                credentialsPa.create(
                    AccountCredentialsBo(
                        accountId = action.accountId,
                        type = CredentialTypes.password,
                        value = action.newPassword,
                        expiration = null
                    )
                )
            }

        auditor.auditCustom(executor) { "password change accountId=${action.accountId} executorId=${executor.accountId}" }

        return ActionStatusBo()
    }

    // -------------------------------------------------------------------------
    // Internal functions
    // -------------------------------------------------------------------------

    /**
     * Perform password based authentication. Increments success/fail counters according
     * to the result. Locks the account if login fails surpass [ModuleSettings.maxFailedLogins].
     *
     * @param   executor   The executor of the authentication.
     * @param   accountId  The account id to authenticate.
     * @param   password   The password to authenticate with.
     *
     * @throws  NoSuchElementException   When the account does not exists.
     * @throws  Unauthorized             When the login fails.
     */
    open fun authenticate(executor: Executor, accountId: EntityId<AccountPrivateBo>, password: String) {

        val state = statePa.readOrNull(accountId)
        val credentials = credentialsPa.read(accountId, CredentialTypes.password)

        val result = when {
            state == null -> throw Unauthorized("NoState")
            ! state.validated -> Unauthorized("NotValidated")
            state.locked -> Unauthorized("Locked", UnauthorizedData(true))
            state.expired -> Unauthorized("Expired")
            state.anonymized -> Unauthorized("Anonymized")
            ! BCrypt.checkpw(password, credentials.value.value) -> Unauthorized("InvalidCredentials")
            else -> null
        }

        if (result != null) {
            state.loginFailCount ++
            state.lastLoginFail = Clock.System.now()
            state.locked = state.locked || (state.loginFailCount > settings.maxFailedLogins)

            statePa.update(state)
            pa.commit()

            auditor.auditCustom(executor) { "login fail accountId=${accountId} reason=${result.reason}" }
            pa.commit() // the two commits are intentional, we don't want to loose the locking mechanism in case audit fails

            throw result
        }

        state.lastLoginSuccess = Clock.System.now()
        state.loginSuccessCount ++

        state.loginFailCount = 0

        auditor.auditCustom(executor) { ("login success accountId=${accountId}") }

        statePa.update(state)
    }

    override fun authenticate(executor: Executor, accountName: String, password: Secret): AccountPublicBo {
        val account = pa.readByNameOrNull(accountName) ?: throw Unauthorized("UnknownAccount")
        authenticate(executor, account.id, password.value)
        return account.toPublic()
    }

    open fun AccountPrivateBo.toPublic() = AccountPublicBo(
        accountId = EntityId(id),
        accountName = accountName,
        fullName = fullName,
        email = if (settings.emailInAccountPublic) email else null,
        phone = if (settings.phoneInAccountPublic) phone else null,
        theme = theme,
        locale = locale
    )

    override fun anonymous() = anonymous

    override fun readPublic(account: EntityId<out BaseBo>) = pa.read(EntityId(account)).toPublic()

    override fun roles(accountId: EntityId<out BaseBo>): List<Pair<EntityId<out BaseBo>, String>> {
        return roleBl.rolesOf(EntityId(accountId))
    }

    open fun byName(accountName: String) = pa.readByName(accountName)

}