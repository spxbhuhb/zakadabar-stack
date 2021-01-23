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
import zakadabar.demo.backend.account.account.AccountPrivateDao
import zakadabar.demo.backend.speed.SpeedDao
import zakadabar.demo.data.ShipDto
import zakadabar.demo.data.ShipSpeeds
import zakadabar.demo.data.ShipsByName
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.util.Executor

object ShipBackend : RecordBackend<ShipDto>(blobTable = ShipImageTable, recordTable = ShipTable) {

    override val dtoClass = ShipDto::class

    override fun onModuleLoad() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                ShipTable,
                ShipImageTable
            )
        }
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.blob()
        route.query(ShipsByName::class, ShipBackend::query)
        route.query(ShipSpeeds::class, ShipBackend::query)
    }

    private fun query(executor: Executor, query: ShipsByName) = transaction {
        ShipTable
            .select { ShipTable.name like "%${query.name}%" }
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
            fromDto(dto)
        }.toDto()
    }

    private fun ShipDao.fromDto(dto: ShipDto): ShipDao {
        name = dto.name
        speed = SpeedDao[dto.speed]
        captain = AccountPrivateDao[dto.captain]
        description = dto.description
        return this
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        ShipDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: ShipDto) = transaction {
        ShipDao[dto.id].fromDto(dto).toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        ShipDao[recordId].delete()
    }
}