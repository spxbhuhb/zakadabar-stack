/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.principal

import io.ktor.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.Server
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantTable
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.get
import zakadabar.stack.data.builtin.ActionStatusBo
import zakadabar.stack.data.builtin.account.PasswordChangeAction
import zakadabar.stack.data.builtin.account.PrincipalBo
import zakadabar.stack.data.builtin.account.RoleBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.BCrypt

object PrincipalBackend : EntityBackend<PrincipalBo>() {

    override val boClass = PrincipalBo::class

    var maxFailCount = 5

    override var logActions = false

    override fun onModuleLoad() {
        Sql.tables += PrincipalTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.action(PasswordChangeAction::class, ::action)
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        PrincipalTable
            .selectAll()
            .map(PrincipalTable::toBo)
    }

    override fun create(executor: Executor, bo: PrincipalBo) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        PrincipalDao.new {
            validated = bo.validated
            locked = bo.locked
            expired = bo.expired
            loginFailCount = 0
        }.toBo()
    }

    override fun read(executor: Executor, entityId: EntityId<PrincipalBo>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        PrincipalDao[entityId].toBo()
    }

    override fun update(executor: Executor, bo: PrincipalBo) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        val dao = PrincipalDao[bo.id.toLong()]

        if (dao.locked && ! bo.locked) {
            dao.loginFailCount = 0
        }

        dao.locked = bo.locked

        dao.toBo()
    }

    override fun delete(executor: Executor, entityId: EntityId<PrincipalBo>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        PrincipalDao[entityId].delete()
    }

    private fun action(executor: Executor, action: PasswordChangeAction) = transaction {

        authorize(executor, StackRoles.siteMember)

        val (accountBo, principalId) = Server.findAccountById(action.accountId)

        val principalDao = PrincipalDao[principalId]

        if (executor.accountId == action.accountId) {
            try {
                authenticate(principalId, action.oldPassword.value)
            } catch (ex: Exception) {
                return@transaction ActionStatusBo(false)
            }
        } else {
            authorize(executor, StackRoles.securityOfficer)
        }

        principalDao.credentials = encrypt(action.newPassword.value)

        logger.info("${executor.accountId}: ACTION PasswordChangeAction accountId=${action.accountId} accountName=${accountBo.accountName}")

        ActionStatusBo()
    }

    fun encrypt(password: String) = BCrypt.hashpw(password, BCrypt.gensalt())

    class InvalidCredentials : Exception()
    class AccountNotValidatedException : Exception()
    class AccountLockedException : Exception()
    class AccountExpiredException : Exception()

    fun authenticate(principalId: EntityId<PrincipalBo>, password: String) = transaction {

        val principal = PrincipalDao[principalId.toLong()]

        val credentials = principal.credentials ?: throw NoSuchElementException()

        val result = when {
            ! principal.validated -> AccountNotValidatedException()
            principal.locked -> AccountLockedException()
            principal.expired -> AccountExpiredException()
            ! BCrypt.checkpw(password, credentials) -> InvalidCredentials()
            else -> null
        }

        if (result != null) {
            principal.loginFailCount ++
            principal.lastLoginFail = Clock.System.now().toJavaInstant()
            principal.locked = principal.locked || (principal.loginFailCount > maxFailCount)
            commit()
            throw result
        }

        principal.lastLoginSuccess = Clock.System.now().toJavaInstant()
        principal.loginSuccessCount ++

        principal.lastLoginFail = null
        principal.loginFailCount = 0

        principal
    }

    fun roles(principalId: EntityId<PrincipalBo>) = transaction {
        val roleIds = mutableListOf<EntityId<RoleBo>>()
        val roleNames = mutableListOf<String>()

        RoleGrantTable
            .join(RoleTable, JoinType.INNER, additionalConstraint = { RoleTable.id eq RoleGrantTable.role })
            .slice(RoleTable.name, RoleTable.id)
            .select { RoleGrantTable.principal eq principalId.toLong() }
            .forEach {
                roleIds += EntityId(it[RoleTable.id].value)
                roleNames += it[RoleTable.name]
            }

        roleIds to roleNames
    }

}