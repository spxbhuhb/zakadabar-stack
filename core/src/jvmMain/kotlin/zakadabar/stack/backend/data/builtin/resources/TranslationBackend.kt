/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.resources

import io.ktor.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.get
import zakadabar.stack.data.builtin.resources.TranslationBo
import zakadabar.stack.data.builtin.resources.TranslationsByLocale
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor

object TranslationBackend : EntityBackend<TranslationBo>() {

    override val boClass = TranslationBo::class

    override fun onModuleLoad() {
        Sql.tables +=  TranslationTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.query(TranslationsByLocale::class, ::query)
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        TranslationTable
            .selectAll()
            .map(TranslationTable::toBo)
    }

    override fun create(executor: Executor, bo: TranslationBo) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        TranslationDao.new {
            name = bo.name
            locale = bo.locale
            value = bo.value
        }.toBo()
    }

    override fun read(executor: Executor, entityId: EntityId<TranslationBo>) = transaction {

        authorize(executor, StackRoles.siteMember)

        TranslationDao[entityId].toBo()
    }

    override fun update(executor: Executor, bo: TranslationBo) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        val dao = TranslationDao[bo.id]
        with(dao) {
            name = bo.name
            locale = bo.locale
            value = bo.value
        }
        dao.toBo()
    }

    override fun delete(executor: Executor, entityId: EntityId<TranslationBo>) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        TranslationDao[entityId].delete()
    }

    private fun query(executor: Executor, query: TranslationsByLocale) = transaction {

        // TODO this should be governed by a server parameter
        authorize(true) // this makes translations available for the public

        TranslationTable
            .select { TranslationTable.locale eq query.locale }
            .map { TranslationTable.toBo(it) }
    }
}