/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.stack.backend.data.builtin.resources

import io.ktor.features.*
import io.ktor.routing.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.builtin.role.RoleTable
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.builtin.resources.SettingDto
import zakadabar.stack.util.Executor

object SettingBackend : RecordBackend<SettingDto>() {

    override val dtoClass = SettingDto::class

    override fun onModuleLoad() {
        + SettingTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(true) // authorized for all users, filter settings by roles later

        val roleIds = executor.roleIds.map { EntityID(it, RoleTable) } + null

        SettingTable
            .select { SettingTable.role inList roleIds }
            .map(SettingTable::toDto)
    }

    override fun create(executor: Executor, dto: SettingDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        SettingDao.new {
            name = dto.name
            value = dto.value
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {

        authorize(true) // authorize for all users, throw not found if role is missing

        val dao = SettingDao[recordId]

        dao.role?.id?.let { if (! executor.hasRole(it.value)) throw NotFoundException() }

        dao.toDto()
    }

    override fun update(executor: Executor, dto: SettingDto) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        val dao = SettingDao[dto.id]
        with(dao) {
            name = dto.name
            value = dto.value
        }
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) = transaction {

        authorize(executor, StackRoles.siteAdmin)

        SettingDao[recordId].delete()
    }

}