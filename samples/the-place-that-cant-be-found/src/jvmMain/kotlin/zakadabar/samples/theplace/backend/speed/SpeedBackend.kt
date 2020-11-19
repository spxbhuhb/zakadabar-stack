/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.samples.theplace.backend.speed

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.samples.theplace.data.SpeedDto
import zakadabar.stack.backend.data.DtoBackend
import zakadabar.stack.util.Executor

object SpeedBackend : DtoBackend<SpeedDto>() {

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

    override fun read(executor: Executor, id: Long) = transaction {
        SpeedDao[id].toDto()
    }

    override fun update(executor: Executor, dto: SpeedDto) = transaction {
        val dao = SpeedDao[dto.id]
        dao.description = dto.description
        dao.value = dto.value
        dao.toDto()
    }

    override fun delete(executor: Executor, id: Long) {
        SpeedDao[id].delete()
    }
}