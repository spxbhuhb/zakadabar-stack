/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.sea

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.data.SeaDto
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object SeaBackend : RecordBackend<SeaDto>() {

    override val dtoClass = SeaDto::class

    override fun onModuleLoad() {
        + SeaTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        SeaTable
            .selectAll()
            .map(SeaTable::toDto)
    }

    override fun create(executor: Executor, dto: SeaDto) = transaction {
        SeaDao.new {
            name = dto.name
        }.toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<SeaDto>) = transaction {
        SeaDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: SeaDto) = transaction {
        val dao = SeaDao[dto.id]
        dao.name = dto.name
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<SeaDto>) {
        SeaDao[recordId].delete()
    }
}