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
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.resources.TranslationDto
import zakadabar.stack.data.builtin.resources.TranslationsByLocale
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object TranslationBackend : RecordBackend<TranslationDto>() {

    override val dtoClass = TranslationDto::class

    override fun onModuleLoad() {
        + TranslationTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.query(TranslationsByLocale::class, ::query)
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        TranslationTable
            .selectAll()
            .map(TranslationTable::toDto)
    }

    override fun create(executor: Executor, dto: TranslationDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        TranslationDao.new {
            name = dto.name
            locale = dto.locale
            value = dto.value
        }.toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<TranslationDto>) = transaction {

        authorize(executor, StackRoles.siteMember)

        TranslationDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: TranslationDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        val dao = TranslationDao[dto.id]
        with(dao) {
            name = dto.name
            locale = dto.locale
            value = dto.value
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<TranslationDto>) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        TranslationDao[recordId].delete()
    }

    private fun query(executor: Executor, query: TranslationsByLocale) = transaction {

        // TODO this should be governed by a server parameter
        authorize(true) // this makes translations available for the public

        TranslationTable
            .select { TranslationTable.locale eq query.locale }
            .map { TranslationTable.toDto(it) }
    }
}