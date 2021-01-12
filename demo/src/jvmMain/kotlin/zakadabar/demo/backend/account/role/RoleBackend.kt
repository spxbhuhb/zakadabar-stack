/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.account.role

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.data.account.RoleDto
import zakadabar.stack.backend.RecordBackend
import zakadabar.stack.util.Executor

object RoleBackend : RecordBackend<RoleDto>() {

    override val dtoClass = RoleDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                RoleTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        RoleTable
            .selectAll()
            .map(RoleTable::toDto)
    }

    override fun create(executor: Executor, dto: RoleDto) = transaction {
        RoleDao.new {
            name = dto.name
            description = dto.description
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        RoleDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: RoleDto) = transaction {
        val dao = RoleDao[dto.id]
        with(dao) {
            name = dto.name
            description = dto.description
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        RoleDao[recordId].delete()
    }
}