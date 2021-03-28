/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.resources

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.resources.SettingStringDto
import zakadabar.stack.util.Executor

object SettingStringBackend : RecordBackend<SettingStringDto>() {

    override val dtoClass = SettingStringDto::class

    override fun onModuleLoad() {
        + SettingStringTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        SettingStringTable
            .selectAll()
            .map(SettingStringTable::toDto)
    }

    override fun create(executor: Executor, dto: SettingStringDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        SettingStringDao.new {
            name = dto.name
            value = dto.value
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {

        authorize(executor, StackRoles.siteMember)

        SettingStringDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: SettingStringDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        val dao = SettingStringDao[dto.id]
        with(dao) {
            name = dto.name
            value = dto.value
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        SettingStringDao[recordId].delete()
    }

}