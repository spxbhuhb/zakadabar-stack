/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.resources

import io.ktor.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.resources.LocaleStringDto
import zakadabar.stack.data.builtin.resources.StringsByLocale
import zakadabar.stack.util.Executor

object LocaleStringBackend : RecordBackend<LocaleStringDto>() {

    override val dtoClass = LocaleStringDto::class

    override fun onModuleLoad() {
        + LocaleStringTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.query(StringsByLocale::class, ::query)
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        LocaleStringTable
            .selectAll()
            .map(LocaleStringTable::toDto)
    }

    override fun create(executor: Executor, dto: LocaleStringDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        LocaleStringDao.new {
            name = dto.name
            locale = dto.locale
            value = dto.value
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {

        authorize(executor, StackRoles.siteMember)

        LocaleStringDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: LocaleStringDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        val dao = LocaleStringDao[dto.id]
        with(dao) {
            name = dto.name
            locale = dto.locale
            value = dto.value
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        LocaleStringDao[recordId].delete()
    }

    private fun query(executor: Executor, query: StringsByLocale) = transaction {

        // TODO this should be governed by a server parameter
        authorize(true) // this makes translations available for the public

        LocaleStringTable
            .select { LocaleStringTable.locale eq query.locale }
            .map { LocaleStringTable.toDto(it) }
    }
}