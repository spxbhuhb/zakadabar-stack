/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.account.rolegrant

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.backend.account.account.AccountDao
import zakadabar.demo.backend.account.role.RoleDao
import zakadabar.demo.data.account.RoleGrantDto
import zakadabar.stack.backend.RecordBackend
import zakadabar.stack.util.Executor

object RoleGrantBackend : RecordBackend<RoleGrantDto>() {

    override val dtoClass = RoleGrantDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                RoleGrantTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        RoleGrantTable
            .selectAll()
            .map(RoleGrantTable::toDto)
    }

    override fun create(executor: Executor, dto: RoleGrantDto) = transaction {
        RoleGrantDao.new {
            account = AccountDao[dto.account]
            role = RoleDao[dto.role]
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        RoleGrantDao[recordId].toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        RoleGrantDao[recordId].delete()
    }
}