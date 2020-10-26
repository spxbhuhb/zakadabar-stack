/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.samples.holygrail.backend.rabbit

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.samples.holygrail.backend.rabbit.data.RabbitDao
import zakadabar.samples.holygrail.backend.rabbit.data.RabbitTable
import zakadabar.samples.holygrail.data.rabbit.Color
import zakadabar.samples.holygrail.data.rabbit.RabbitColors
import zakadabar.samples.holygrail.data.rabbit.RabbitDto
import zakadabar.samples.holygrail.data.rabbit.RabbitSearch
import zakadabar.stack.backend.data.DtoBackend
import zakadabar.stack.util.Executor

object RabbitBackend : DtoBackend<RabbitDto>() {

    override val dtoClass = RabbitDto::class

    override fun init() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                RabbitTable
            )
        }
    }

    override fun install(route: Route) {
        route.crud()
        route.query(RabbitSearch::class, RabbitBackend::query)
        route.query(RabbitColors::class, RabbitBackend::query)
    }

    private fun query(executor: Executor, query: RabbitSearch) = transaction {
        RabbitTable
            .select { RabbitTable.name like query.name }
            .map(RabbitTable::toDto)
    }

    private fun query(executor: Executor, query: RabbitColors) = transaction {
        RabbitTable
            .slice(RabbitTable.color)
            .select { RabbitTable.name inList query.rabbitNames }
            .distinct()
            .map { Color(it[RabbitTable.color]) }
    }

    override fun all(executor: Executor) = transaction {
        RabbitTable
            .selectAll()
            .map(RabbitTable::toDto)
    }

    override fun create(executor: Executor, dto: RabbitDto) = transaction {
        RabbitDao.new {
            name = dto.name
            color = dto.color
        }.toDto()
    }

    override fun read(executor: Executor, id: Long) = transaction {
        RabbitDao[id].toDto()
    }

    override fun update(executor: Executor, dto: RabbitDto) = transaction {
        val dao = RabbitDao[dto.id]
        dao.name = dto.name
        dao.color = dto.color
        dao.toDto()
    }

    override fun delete(executor: Executor, id: Long) {
        RabbitDao[id].delete()
    }
}