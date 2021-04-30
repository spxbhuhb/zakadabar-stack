/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.marina.backend.port

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.marina.backend.sea.SeaDao
import zakadabar.demo.marina.data.PortDto
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.record.RecordId
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

    override fun read(executor: Executor, recordId: RecordId<PortDto>) = transaction {
        PortDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: PortDto) = transaction {
        with(PortDao[dto.id]) {
            name = dto.name
            sea = SeaDao[dto.sea]
            toDto()
        }
    }

    override fun delete(executor: Executor, recordId: RecordId<PortDto>) {
        PortDao[recordId].delete()
    }
}