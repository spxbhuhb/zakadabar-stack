/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.examples.backend.builtin

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.examples.data.builtin.ExampleReferenceDto
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object ExampleReferenceBackend : EntityBackend<ExampleReferenceDto>() {

    override val boClass = ExampleReferenceDto::class

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

        authorize(true)

        ExampleReferenceTable
            .selectAll()
            .map(ExampleReferenceTable::toDto)
    }

    override fun create(executor: Executor, dto: ExampleReferenceDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        ExampleReferenceDao.new { fromDto(dto) }
            .toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<ExampleReferenceDto>) = transaction {

        authorize(true)

        ExampleReferenceDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: ExampleReferenceDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        ExampleReferenceDao[dto.id]
            .fromDto(dto)
            .toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<ExampleReferenceDto>) = transaction {

        authorize(executor, StackRoles.siteMember)

        ExampleReferenceDao[recordId].delete()
    }
}