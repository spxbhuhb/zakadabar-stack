/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.account.account

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.data.account.AccountDto
import zakadabar.stack.backend.data.RecordBackend
import zakadabar.stack.util.Executor

object AccountBackend : RecordBackend<AccountDto>() {

    override val dtoClass = AccountDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                AccountTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        AccountTable
            .selectAll()
            .map(AccountTable::toDto)
    }

    override fun create(executor: Executor, dto: AccountDto) = transaction {
        val avatarId = dto.avatar
        AccountDao.new {
            accountName = dto.accountName
            email = dto.email
            fullName = dto.fullName
            displayName = dto.displayName
            organizationName = dto.organizationName
            avatar = avatarId?.let { AccountImageDao[avatarId] }
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        AccountDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: AccountDto) = transaction {
        val avatarId = dto.avatar
        val dao = AccountDao[dto.id]
        with(dao) {
            accountName = dto.accountName
            email = dto.email
            fullName = dto.fullName
            displayName = dto.displayName
            organizationName = dto.organizationName
            avatar = avatarId?.let { AccountImageDao[avatarId] }
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        AccountDao[recordId].delete()
    }
}