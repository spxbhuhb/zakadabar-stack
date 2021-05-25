package zakadabar.lib.accounts.backend

import kotlinx.datetime.Clock
import zakadabar.lib.accounts.data.ModuleSettingsBo
import zakadabar.lib.accounts.data.PasswordChangeAction
import zakadabar.lib.accounts.data.PrincipalBo
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize.SimpleRoleAuthorizer
import zakadabar.stack.backend.authorize.authorize
import zakadabar.stack.backend.data.builtin.resources.setting
import zakadabar.stack.backend.data.entity.EntityBusinessLogicBase
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.BCrypt
import zakadabar.stack.util.Executor
import zakadabar.stack.util.PublicApi

class PrincipalBl : EntityBusinessLogicBase<PrincipalBo>(
    boClass = PrincipalBo::class
) {

    private val settings by setting<ModuleSettingsBo>("zakadabar.lib.accounts")

    override val pa = PrincipalExposedPaGen()

    override val authorizer = SimpleRoleAuthorizer<PrincipalBo> {
        all = StackRoles.securityOfficer
        action(PasswordChangeAction::class, StackRoles.siteMember)
    }

    override val router = router {
        action(PasswordChangeAction::class, ::action)
    }

    override val auditor = auditor {
        includeData = false
    }

    /**
     * Updates the principal. This function updates the locked flag (with login fail count)
     * only. Other fields cannot be updated.
     */
    override fun update(executor: Executor, bo: PrincipalBo) : PrincipalBo {

        val db = pa.read(bo.id)

        if (db.locked && ! bo.locked) {
            db.loginFailCount = 0
        }

        db.locked = bo.locked

        pa.update(db)

        return bo
    }

    /**
     * Changes the password of an account. Security officers are allowed to change any
     * passwords without supplying the old password. Exception is their own account
     * which can be changed only if the old password is provided and correct.
     */
    private fun action(executor: Executor, action: PasswordChangeAction) : ActionStatusBo {

        val (accountBo, principalId) = accountPrivateBl.findAccountById(action.accountId)

        val bo = pa.read(principalId)

        if (executor.accountId == action.accountId) {
            try {
                authenticate(principalId, action.oldPassword.value)
            } catch (ex: Exception) {
                return ActionStatusBo(false)
            }
        } else {
            authorize(executor, StackRoles.securityOfficer)
        }

        bo.credentials = action.newPassword

        pa.update(bo)

        auditor.auditCustom(executor) { "password change accountId=${action.accountId} accountName=${accountBo.accountName}"}

        return ActionStatusBo()
    }

    class InvalidCredentials : Exception()
    class AccountNotValidatedException : Exception()
    class AccountLockedException : Exception()
    class AccountExpiredException : Exception()

    /**
     * Authenticates a user. Increments counters as per the result.
     */
    @PublicApi
    fun authenticate(principalId: EntityId<PrincipalBo>, password: String) {

        val principal = pa.read(principalId)

        val credentials = principal.credentials ?: throw NoSuchElementException()

        val result = when {
            ! principal.validated -> AccountNotValidatedException()
            principal.locked -> AccountLockedException()
            principal.expired -> AccountExpiredException()
            ! BCrypt.checkpw(password, credentials.value) -> InvalidCredentials()
            else -> null
        }

        if (result != null) {
            principal.loginFailCount ++
            principal.lastLoginFail = Clock.System.now()
            principal.locked = principal.locked || (principal.loginFailCount > settings.maxFailedLogins)
            pa.commit()
            throw result
        }

        principal.lastLoginSuccess = Clock.System.now()
        principal.loginSuccessCount ++

        principal.loginFailCount = 0

        pa.commit()
    }

}