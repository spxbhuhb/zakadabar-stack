/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.principal

import io.ktor.features.*
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
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantTable
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.ActionStatusDto
import zakadabar.stack.data.builtin.account.PasswordChangeAction
import zakadabar.stack.data.builtin.account.PrincipalDto
import zakadabar.stack.data.builtin.account.RoleDto
import zakadabar.stack.data.record.LongRecordId
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.BCrypt
import zakadabar.stack.util.Executor

object PrincipalBackend : RecordBackend<PrincipalDto>() {

    override val dtoClass = PrincipalDto::class

    var maxFailCount = 5

    override var logActions = false

    override fun onModuleLoad() {
        + PrincipalTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.action(PasswordChangeAction::class, ::action)
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        PrincipalTable
            .selectAll()
            .map(PrincipalTable::toDto)
    }

    override fun create(executor: Executor, dto: PrincipalDto) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        PrincipalDao.new {
            validated = dto.validated
            locked = dto.locked
            expired = dto.expired
            loginFailCount = 0
        }.toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<PrincipalDto>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        PrincipalDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: PrincipalDto) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        val dao = PrincipalDao[dto.id.toLong()]

        if (dao.locked && ! dto.locked) {
            dao.loginFailCount = 0
        }

        dao.locked = dto.locked

        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<PrincipalDto>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        PrincipalDao[recordId].delete()
    }

    private fun action(executor: Executor, action: PasswordChangeAction) = transaction {

        authorize(executor, StackRoles.siteMember)

        val (accountDto, principalId) = Server.findAccountById(action.accountId)

        val principalDao = PrincipalDao[principalId.toLong()]

        if (executor.accountId == action.accountId) {
            try {
                authenticate(principalId, action.oldPassword.value)
            } catch (ex: Exception) {
                return@transaction ActionStatusDto(false)
            }
        } else {
            authorize(executor, StackRoles.securityOfficer)
        }

        principalDao.credentials = encrypt(action.newPassword.value)

        logger.info("${executor.accountId}: ACTION PasswordChangeAction accountId=${action.accountId} accountName=${accountDto.accountName}")

        ActionStatusDto()
    }

    fun encrypt(password: String) = BCrypt.hashpw(password, BCrypt.gensalt())

    class InvalidCredentials : Exception()
    class AccountNotValidatedException : Exception()
    class AccountLockedException : Exception()
    class AccountExpiredException : Exception()

    fun authenticate(principalId: RecordId<PrincipalDto>, password: String) = transaction {

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

    fun roles(principalId: RecordId<PrincipalDto>) = transaction {
        val roleIds = mutableListOf<RecordId<RoleDto>>()
        val roleNames = mutableListOf<String>()

        RoleGrantTable
            .join(RoleTable, JoinType.INNER, additionalConstraint = { RoleTable.id eq RoleGrantTable.role })
            .slice(RoleTable.name, RoleTable.id)
            .select { RoleGrantTable.principal eq principalId.toLong() }
            .forEach {
                roleIds += LongRecordId(it[RoleTable.id].value)
                roleNames += it[RoleTable.name]
            }

        roleIds to roleNames
    }

}