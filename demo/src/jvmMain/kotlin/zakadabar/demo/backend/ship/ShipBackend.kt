/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.ship

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.backend.speed.SpeedDao
import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.ShipSearch
import zakadabar.demo.data.ShipSpeeds
import zakadabar.stack.backend.RecordBackend
import zakadabar.stack.util.Executor

object ShipBackend : RecordBackend<ShipDto>(blobTable = ShipImageTable, recordTable = ShipTable) {

    override val dtoClass = ShipDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                ShipTable,
                ShipImageTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
        route.blob()
        route.query(ShipSearch::class, ShipBackend::query)
        route.query(ShipSpeeds::class, ShipBackend::query)
    }

    private fun query(executor: Executor, query: ShipSearch) = transaction {
        ShipTable
            .select { ShipTable.name like query.name }
            .map(ShipTable::toDto)
    }

    private fun query(executor: Executor, query: ShipSpeeds) = transaction {
        ShipTable
            .slice(ShipTable.speed)
            .select { ShipTable.name inList query.names }
            .distinct()
            .map { it[ShipTable.speed] }
    }

    override fun all(executor: Executor) = transaction {
        ShipTable
            .selectAll()
            .map(ShipTable::toDto)
    }

    override fun create(executor: Executor, dto: ShipDto) = transaction {
        ShipDao.new {
            name = dto.name
            speed = SpeedDao[dto.speed]
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        ShipDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: ShipDto) = transaction {

        val dao = ShipDao[dto.id]
        dao.name = dto.name
        dao.speed = SpeedDao[dto.speed]

        dao.toDto()

    }

    override fun delete(executor: Executor, recordId: Long) {
        ShipDao[recordId].delete()
    }
}