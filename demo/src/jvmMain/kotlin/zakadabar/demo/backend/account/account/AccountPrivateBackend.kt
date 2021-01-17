/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.account.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.data.account.AccountPrivateDto
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.util.Executor

object AccountPrivateBackend : RecordBackend<AccountPrivateDto>() {

    override val dtoClass = AccountPrivateDto::class

    var maxFailCount = 5

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccountPrivateTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        AccountPrivateTable
            .selectAll()
            .map(AccountPrivateTable::toDto)
    }

    override fun create(executor: Executor, dto: AccountPrivateDto) = transaction {

        val newPrincipal = PrincipalDao.new { }

        AccountPrivateDao.new {
            principal = newPrincipal
            fromDto(dto)
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        AccountPrivateDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: AccountPrivateDto) = transaction {
        AccountPrivateDao[dto.id].fromDto(dto).toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        AccountPrivateDao[recordId].delete()
    }

    private fun AccountPrivateDao.fromDto(dto: AccountPrivateDto): AccountPrivateDao {
        val avatarId = dto.avatar

        accountName = dto.accountName
        fullName = dto.fullName
        displayName = dto.displayName
        avatar = avatarId?.let { AccountImageDao[avatarId] }

        organizationName = dto.organizationName
        position = dto.position

        email = dto.email
        phone = dto.phone

        return this
    }

}