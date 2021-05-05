/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.examples.backend.builtin

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.examples.data.builtin.ExampleReferenceDto
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object ExampleReferenceBackend : RecordBackend<zakadabar.lib.examples.data.builtin.ExampleReferenceDto>() {

    override val dtoClass = zakadabar.lib.examples.data.builtin.ExampleReferenceDto::class

    override fun onModuleLoad() {
        + ExampleReferenceTable
    }

    override fun onModuleStart() {
        super.onModuleStart()

        transaction {
            if (ExampleReferenceTable.selectAll().count() == 0L) {
                ExampleReferenceDao.new { name = "first" }
                ExampleReferenceDao.new { name = "second" }
                ExampleReferenceDao.new { name = "third" }
            }
        }
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {
        ExampleReferenceTable
            .selectAll()
            .map(ExampleReferenceTable::toDto)
    }

    override fun create(executor: Executor, dto: zakadabar.lib.examples.data.builtin.ExampleReferenceDto) = transaction {
        ExampleReferenceDao.new { fromDto(dto) }
            .toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<zakadabar.lib.examples.data.builtin.ExampleReferenceDto>) = transaction {
        ExampleReferenceDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: zakadabar.lib.examples.data.builtin.ExampleReferenceDto) = transaction {
        ExampleReferenceDao[dto.id]
            .fromDto(dto)
            .toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<zakadabar.lib.examples.data.builtin.ExampleReferenceDto>) = transaction {
        ExampleReferenceDao[recordId].delete()
    }
}