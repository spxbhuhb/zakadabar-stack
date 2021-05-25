/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.resources

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.get
import zakadabar.stack.data.builtin.resources.LocaleBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor

object LocaleBackend : EntityBackend<LocaleBo>() {

    override val boClass = LocaleBo::class

    override fun onModuleLoad() {
        Sql.tables +=  LocaleTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(true)

        LocaleTable
            .selectAll()
            .map(LocaleTable::toBo)
    }

    override fun create(executor: Executor, bo: LocaleBo) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        LocaleDao.new {
            name = bo.name
            description = bo.description
        }.toBo()
    }

    override fun read(executor: Executor, entityId: EntityId<LocaleBo>) = transaction {

        authorize(true)

        LocaleDao[entityId].toBo()
    }

    override fun update(executor: Executor, bo: LocaleBo) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        val dao = LocaleDao[bo.id]
        with(dao) {
            name = bo.name
            description = bo.description
        }
        dao.toBo()
    }

    override fun delete(executor: Executor, entityId: EntityId<LocaleBo>) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        LocaleDao[entityId].delete()
    }

}