/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.demo.backend.lib

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.demo.data.builtin.BuiltinDto
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.DataConflictException
import zakadabar.stack.util.Executor

object BuiltinBackend : RecordBackend<BuiltinDto>() {

    override val dtoClass = BuiltinDto::class

    override fun onModuleLoad() {
        + BuiltinTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        BuiltinTable
            .selectAll()
            .map(BuiltinTable::toDto)
    }

    override fun create(executor: Executor, dto: BuiltinDto) = transaction {
        if (dto.stringValue == "conflict") throw DataConflictException("stringValueConflict")

        BuiltinDao
            .new { fromDto(dto) }
            .toDto()
    }

    override fun read(executor: Executor, recordId: Long) = transaction {
        BuiltinDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: BuiltinDto) = transaction {
        BuiltinDao[dto.id]
            .fromDto(dto)
            .toDto()
    }

    override fun delete(executor: Executor, recordId: Long) = transaction {
        BuiltinDao[recordId].delete()
    }
}