/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.rolegrant

import io.ktor.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.builtin.role.RoleDao
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.account.RoleGrantDto
import zakadabar.stack.data.builtin.account.RoleGrantsByPrincipal
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object RoleGrantBackend : RecordBackend<RoleGrantDto>() {

    override val dtoClass = RoleGrantDto::class

    override fun onModuleLoad() {
        + RoleGrantTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.query(RoleGrantsByPrincipal::class, ::query)
    }

    private fun query(executor: Executor, query: RoleGrantsByPrincipal) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantTable
            .select { RoleGrantTable.principal eq query.principal.toLong() }
            .map(RoleGrantTable::toDto)

    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantTable
            .selectAll()
            .map(RoleGrantTable::toDto)
    }

    override fun create(executor: Executor, dto: RoleGrantDto) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantDao.new {
            principal = PrincipalDao[dto.principal]
            role = RoleDao[dto.role]
        }.toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<RoleGrantDto>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantDao[recordId].toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<RoleGrantDto>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantDao[recordId].delete()
    }

}