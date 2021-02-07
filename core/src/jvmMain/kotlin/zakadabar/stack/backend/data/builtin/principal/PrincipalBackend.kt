/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.builtin.rolegrant.RoleGrantTable
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.PrincipalDto
import zakadabar.stack.util.BCrypt
import zakadabar.stack.util.Executor

object PrincipalBackend : RecordBackend<PrincipalDto>() {

    override val dtoClass = PrincipalDto::class

    var maxFailCount = 5

    override fun onModuleLoad() {
        + PrincipalTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        PrincipalTable
            .selectAll()
            .map(PrincipalTable::toDto)
    }

    override fun create(executor: Executor, dto: PrincipalDto) = transaction {
        PrincipalDao.new {
            fromDto(dto)
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        PrincipalDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: PrincipalDto) = transaction {
        PrincipalDao[dto.id].fromDto(dto).toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        PrincipalDao[recordId].delete()
    }

    private fun PrincipalDao.fromDto(dto: PrincipalDto): PrincipalDao {

        validated = dto.validated
        locked = dto.locked
        expired = dto.expired

        lastLoginSuccess = dto.lastLoginSuccess?.toJavaInstant()
        loginSuccessCount = dto.loginSuccessCount

        lastLoginFail = dto.lastLoginFail?.toJavaInstant()
        loginFailCount = dto.loginFailCount

        return this
    }

    fun encrypt(password: String) = BCrypt.hashpw(password, BCrypt.gensalt())

    class AccountNotValidatedException : Exception()
    class AccountLockedException : Exception()
    class AccountExpiredException : Exception()

    fun authenticate(principalId: Long, password: String) = transaction {

        val principal = PrincipalDao[principalId]

        val credentials = principal.credentials ?: throw NoSuchElementException()

        if (! BCrypt.checkpw(password, credentials)) {
            principal.loginFailCount ++
            principal.lastLoginFail = Clock.System.now().toJavaInstant()
            principal.locked = principal.loginFailCount > maxFailCount
            commit()
            throw NotFoundException() // intentional
        }

        if (! principal.validated) throw AccountNotValidatedException()
        if (principal.locked) throw AccountLockedException()
        if (principal.expired) throw AccountExpiredException()

        principal.lastLoginSuccess = Clock.System.now().toJavaInstant()
        principal.loginSuccessCount ++

        principal.lastLoginFail = null
        principal.loginFailCount = 0

        principal
    }

    fun roles(principalId: Long) = transaction {
        RoleGrantTable
            .join(RoleTable, JoinType.INNER, additionalConstraint = { RoleTable.id eq RoleGrantTable.role })
            .slice(RoleTable.name)
            .select { RoleGrantTable.principal eq principalId }
            .map { it[RoleTable.name] }
    }

}