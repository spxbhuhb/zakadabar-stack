/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.rolegrant

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.builtin.role.RoleDao
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.RoleGrantDto
import zakadabar.stack.util.Executor

object RoleGrantBackend : RecordBackend<RoleGrantDto>() {

    override val dtoClass = RoleGrantDto::class

    override fun onModuleLoad() {
        + RoleGrantTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        RoleGrantTable
            .selectAll()
            .map(RoleGrantTable::toDto)
    }

    override fun create(executor: Executor, dto: RoleGrantDto) = transaction {
        RoleGrantDao.new {
            principal = PrincipalDao[dto.principal]
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