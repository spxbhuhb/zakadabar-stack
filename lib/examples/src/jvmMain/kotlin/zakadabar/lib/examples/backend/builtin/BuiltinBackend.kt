/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.examples.backend.builtin

import io.ktor.routing.*
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.examples.data.builtin.BuiltinDto
import zakadabar.lib.examples.data.builtin.ExampleQuery
import zakadabar.lib.examples.data.builtin.ExampleResult
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.backend.data.recordId
import zakadabar.stack.data.DataConflictException
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object BuiltinBackend : RecordBackend<BuiltinDto>() {

    override val dtoClass = BuiltinDto::class

    override fun onModuleLoad() {
        + BuiltinTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
        route.query(ExampleQuery::class, BuiltinBackend::query)
    }

    override fun all(executor: Executor) = transaction {

        authorize(true)

        BuiltinTable
            .selectAll()
            .map(BuiltinTable::toDto)
    }

    override fun create(executor: Executor, dto: BuiltinDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        if (dto.stringValue == "conflict") throw DataConflictException("stringValueConflict")

        BuiltinDao
            .new { fromDto(dto) }
            .toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<BuiltinDto>) = transaction {

        authorize(true)

        BuiltinDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: BuiltinDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        BuiltinDao[dto.id]
            .fromDto(dto)
            .toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<BuiltinDto>) = transaction {

        authorize(executor, StackRoles.siteMember)

        BuiltinDao[recordId].delete()
    }

    private fun query(executor: Executor, query: ExampleQuery) = transaction {

        authorize(true)

        val select = BuiltinTable
            .slice(
                BuiltinTable.id,
                BuiltinTable.booleanValue,
                BuiltinTable.enumSelectValue,
                BuiltinTable.intValue,
                BuiltinTable.stringValue
            )
            .selectAll()

        query.booleanValue?.let { select.andWhere { BuiltinTable.booleanValue eq it } }
        query.enumSelectValue?.let { select.andWhere { BuiltinTable.enumSelectValue eq it } }
        query.intValue?.let { select.andWhere { BuiltinTable.intValue eq it } }
        query.stringValue?.let { select.andWhere { BuiltinTable.stringValue eq it } }

        select.map {
            ExampleResult(
                recordId = it[BuiltinTable.id].recordId(),
                booleanValue = it[BuiltinTable.booleanValue],
                enumSelectValue = it[BuiltinTable.enumSelectValue],
                intValue = it[BuiltinTable.intValue],
                stringValue = it[BuiltinTable.stringValue]
            )
        }
    }
}