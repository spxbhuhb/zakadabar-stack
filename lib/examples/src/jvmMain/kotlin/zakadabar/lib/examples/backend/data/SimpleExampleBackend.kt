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
import zakadabar.stack.backend.data.entity.EntityBackend
import zakadabar.stack.backend.data.get
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.util.Executor

object SimpleExampleBackend : EntityBackend<SimpleExampleDto>() {

    override val boClass = SimpleExampleDto::class

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

    override fun create(executor: Executor, bo: SimpleExampleDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao
            .new { fromDto(bo) }
            .toDto()
    }

    override fun read(executor: Executor, entityId: EntityId<SimpleExampleDto>) = transaction {

        authorize(true)

        SimpleExampleDao[entityId].toDto()
    }

    override fun update(executor: Executor, bo: SimpleExampleDto) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao[bo.id]
            .fromDto(bo)
            .toDto()
    }

    override fun delete(executor: Executor, entityId: EntityId<SimpleExampleDto>) = transaction {

        authorize(executor, StackRoles.siteMember)

        SimpleExampleDao[entityId].delete()
    }

}