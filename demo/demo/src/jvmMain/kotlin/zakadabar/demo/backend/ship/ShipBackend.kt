/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.ship

import io.ktor.routing.*
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.backend.port.PortDao
import zakadabar.demo.backend.port.PortTable
import zakadabar.demo.backend.sea.SeaTable
import zakadabar.demo.backend.speed.SpeedDao
import zakadabar.demo.backend.speed.SpeedTable
import zakadabar.demo.data.ship.SearchShipsQuery
import zakadabar.demo.data.ship.SearchShipsResult
import zakadabar.demo.data.ship.ShipDto
import zakadabar.stack.backend.data.builtin.account.AccountPrivateDao
import zakadabar.stack.backend.data.builtin.account.AccountPrivateTable
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.backend.data.recordId
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object ShipBackend : RecordBackend<ShipDto>(blobTable = ShipImageTable, recordTable = ShipTable) {

    override val dtoClass = ShipDto::class

    override fun onModuleLoad() {
        + ShipTable
        + ShipImageTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.blob()
        route.query(SearchShipsQuery::class, ::query)
    }

    private fun query(executor: Executor, query: SearchShipsQuery) = transaction {
        val select = ShipTable
            .join(PortTable, JoinType.LEFT, additionalConstraint = { PortTable.id eq ShipTable.port })
            .join(SeaTable, JoinType.LEFT, additionalConstraint = { SeaTable.id eq PortTable.sea })
            .join(SpeedTable, JoinType.INNER, additionalConstraint = { SpeedTable.id eq ShipTable.speed })
            .join(AccountPrivateTable, JoinType.INNER, additionalConstraint = { AccountPrivateTable.id eq ShipTable.captain })
            .slice(
                ShipTable.id,
                ShipTable.name,
                PortTable.name,
                AccountPrivateTable.fullName
            )
            .selectAll()

        query.name?.let { select.andWhere { ShipTable.name like "%${query.name}%" } }
        query.speed?.let { select.andWhere { SpeedTable.id eq query.speed?.toLong() } }
        query.sea?.let { select.andWhere { SeaTable.id eq query.sea?.toLong() } }
        query.port?.let { select.andWhere { PortTable.id eq query.port?.toLong() } }

        select.map {
            SearchShipsResult(
                shipId = it[ShipTable.id].recordId(),
                name = it[ShipTable.name],
                port = it[PortTable.name],
                captain = it[AccountPrivateTable.fullName]
            )
        }
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
        hasFlag = dto.hasPirateFlag
        port = dto.port?.let { PortDao[it] }
        return this
    }

    override fun read(executor: Executor, recordId: RecordId<ShipDto>) = transaction {
        ShipDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: ShipDto) = transaction {
        ShipDao[dto.id].fromDto(dto).toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<ShipDto>) = transaction {
        ShipDao[recordId].delete()
    }
}