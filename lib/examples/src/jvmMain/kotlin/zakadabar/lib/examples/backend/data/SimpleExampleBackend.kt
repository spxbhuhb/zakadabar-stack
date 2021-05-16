/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("UNUSED_PARAMETER", "unused")

package zakadabar.lib.examples.backend.data

import io.ktor.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.lib.examples.data.SimpleExampleDto
import zakadabar.stack.StackRoles
import zakadabar.stack.backend.authorize
import zakadabar.stack.backend.data.get
import zakadabar.stack.backend.data.record.RecordBackend
import zakadabar.stack.data.record.RecordId
import zakadabar.stack.util.Executor

object SimpleExampleBackend : RecordBackend<SimpleExampleDto>() {

    override val dtoClass = SimpleExampleDto::class

    override fun onModuleLoad() {
        + SimpleExampleTable
    }

    override fun onInstallRoutes(route: Route) {
        route.crud()
    }

    override fun all(executor: Executor) = transaction {

        authorize(true)

        SimpleExampleTable
            .selectAll()
            .map(SimpleExampleTable::toDto)
    }

    override fun create(executor: Executor, dto: SimpleExampleDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao
            .new { fromDto(dto) }
            .toDto()
    }

    override fun read(executor: Executor, recordId: RecordId<SimpleExampleDto>) = transaction {

        authorize(true)

        SimpleExampleDao[recordId].toDto()
    }

    override fun update(executor: Executor, dto: SimpleExampleDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao[dto.id]
            .fromDto(dto)
            .toDto()
    }

    override fun delete(executor: Executor, recordId: RecordId<SimpleExampleDto>) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao[recordId].delete()
    }

}