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
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.data.builtin.principal.PrincipalDao
import zakadabar.stack.backend.data.builtin.role.RoleDao
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.get
import zakadabar.stack.data.builtin.account.RoleGrantBo
import zakadabar.stack.data.builtin.account.RoleGrantsByPrincipal
import zakadabar.stack.data.entity.EntityId

object RoleGrantBackend : EntityBackend<RoleGrantBo>() {

    override val boClass = RoleGrantBo::class

    override fun onModuleLoad() {
        Sql.tables += RoleGrantTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.query(RoleGrantsByPrincipal::class, ::query)
    }

    private fun query(executor: Executor, query: RoleGrantsByPrincipal) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantTable
            .select { RoleGrantTable.principal eq query.principal.toLong() }
            .map(RoleGrantTable::toBo)

    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantTable
            .selectAll()
            .map(RoleGrantTable::toBo)
    }

    override fun create(executor: Executor, bo: RoleGrantBo) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantDao.new {
            principal = PrincipalDao[bo.principal]
            role = RoleDao[bo.role]
        }.toBo()
    }

    override fun read(executor: Executor, entityId: EntityId<RoleGrantBo>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantDao[entityId].toBo()
    }

    override fun delete(executor: Executor, entityId: EntityId<RoleGrantBo>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleGrantDao[entityId].delete()
    }

}