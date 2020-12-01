/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.speed

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.data.SpeedDto
import zakadabar.stack.backend.data.RecordBackend
import zakadabar.stack.util.Executor

object SpeedBackend : RecordBackend<SpeedDto>() {

    override val dtoClass = SpeedDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                SpeedTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        SpeedTable
            .selectAll()
            .map(SpeedTable::toDto)
    }

    override fun create(executor: Executor, dto: SpeedDto) = transaction {
        SpeedDao.new {
            description = dto.description
            value = dto.value
        }.toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        SpeedDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: SpeedDto) = transaction {
        val dao = SpeedDao[dto.id]
        dao.description = dto.description
        dao.value = dto.value
        dao.toDto()
    }

    override fun delete(executor: Executor, recordId: Long) {
        SpeedDao[recordId].delete()
    }
}