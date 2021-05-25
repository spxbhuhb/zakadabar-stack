/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.role

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.get
import zakadabar.stack.data.builtin.account.RoleBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor

object RoleBackend : EntityBackend<RoleBo>() {

    override val boClass = RoleBo::class

    override fun onModuleLoad() {
        Sql.tables +=  RoleTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.siteMember)

        RoleTable
            .selectAll()
            .map(RoleTable::toBo)
    }

    override fun create(executor: Executor, bo: RoleBo) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleDao.new {
            name = bo.name
            description = bo.description
        }.toBo()
    }

    override fun read(executor: Executor, entityId: EntityId<RoleBo>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleDao[entityId].toBo()
    }

    override fun update(executor: Executor, bo: RoleBo) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        val dao = RoleDao[bo.id]
        with(dao) {
            name = bo.name
            description = bo.description
        }
        dao.toBo()
    }

    override fun delete(executor: Executor, entityId: EntityId<RoleBo>) = transaction {

        authorize(executor, StackRoles.securityOfficer)

        RoleDao[entityId].delete()
    }

    fun findForName(roleName: String): EntityId<RoleBo>? = transaction {
        val value = RoleDao
            .find { RoleTable.name eq roleName }
            .firstOrNull()
            ?.id?.value

        if (value == null) null else EntityId(value)
    }
}