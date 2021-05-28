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
import zakadabar.stack.backend.authorize.Executor
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.exposed.Sql
import zakadabar.stack.backend.exposed.get
import zakadabar.stack.data.entity.EntityId

object ExampleReferenceBackend : EntityBackend<ExampleReferenceDto>() {

    override val boClass = ExampleReferenceDto::class

    override fun onModuleLoad() {
        Sql.tables += ExampleReferenceTable
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

    override fun create(executor: Executor, bo: ExampleReferenceDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        ExampleReferenceDao.new { fromDto(bo) }
            .toDto()
    }

    override fun read(executor: Executor, entityId: EntityId<ExampleReferenceDto>) = transaction {

        authorize(true)

        ExampleReferenceDao[entityId].toDto()
    }

    override fun update(executor: Executor, bo: ExampleReferenceDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        ExampleReferenceDao[bo.id]
            .fromDto(bo)
            .toDto()
    }

    override fun delete(executor: Executor, entityId: EntityId<ExampleReferenceDto>) = transaction {

        authorize(executor, StackRoles.siteMember)

        ExampleReferenceDao[entityId].delete()
    }
}