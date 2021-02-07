/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.role

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.RoleDto
import zakadabar.stack.util.Executor

object RoleBackend : RecordBackend<RoleDto>() {

    override val dtoClass = RoleDto::class

    override fun onModuleLoad() {
        + RoleTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleTable
            .selectAll()
            .map(RoleTable::toDto)
    }

    override fun create(executor: Executor, dto: RoleDto) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleDao.new {
            name = dto.name
            description = dto.description
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: RoleDto) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        val dao = RoleDao[dto.id]
        with(dao) {
            name = dto.name
            description = dto.description
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {

        authorize(executor, StackRoles.securityOfficer)

        RoleDao[recordId].delete()
    }
}