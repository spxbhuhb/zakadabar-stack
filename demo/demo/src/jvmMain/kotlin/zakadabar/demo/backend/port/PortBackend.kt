/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.port

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.backend.sea.SeaDao
import zakadabar.demo.data.PortDto
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.util.Executor

object PortBackend : RecordBackend<PortDto>() {

    override val dtoClass = PortDto::class

    override fun onModuleLoad() {
        + PortTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        PortTable
            .selectAll()
            .map(PortTable::toDto)
    }

    override fun create(executor: Executor, dto: PortDto) = transaction {
        PortDao.new {
            name = dto.name
            sea = SeaDao[dto.sea]
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        PortDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: PortDto) = transaction {
        with(PortDao[dto.id]) {
            name = dto.name
            sea = SeaDao[dto.sea]
            toDto()
        }
    }

    override fun delete(executor: Executor, recordId: Long) {
        PortDao[recordId].delete()
    }
}