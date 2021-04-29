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
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.resources.LocaleDto
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object LocaleBackend : RecordBackend<LocaleDto>() {

    override val dtoClass = LocaleDto::class

    override fun onModuleLoad() {
        + LocaleTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(true)

        LocaleTable
            .selectAll()
            .map(LocaleTable::toDto)
    }

    override fun create(executor: Executor, dto: LocaleDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        LocaleDao.new {
            name = dto.name
            description = dto.description
        }.toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<LocaleDto>) = transaction {

        authorize(true)

        LocaleDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: LocaleDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        val dao = LocaleDao[dto.id]
        with(dao) {
            name = dto.name
            description = dto.description
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<LocaleDto>) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        LocaleDao[recordId].delete()
    }

}