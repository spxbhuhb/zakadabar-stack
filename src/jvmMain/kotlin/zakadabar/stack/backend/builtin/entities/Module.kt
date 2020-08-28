/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.builtin.entities

import io.ktor.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.Stack
import zakadabar.stack.backend.builtin.entities.data.EntityTable
import zakadabar.stack.backend.builtin.entities.data.Locks
import zakadabar.stack.backend.builtin.entities.data.Sessions
import zakadabar.stack.backend.builtin.entities.data.SnapshotTable
import zakadabar.stack.backend.extend.BackendModule

object Module : BackendModule() {

    override val uuid = Stack.uuid

    override fun init() = transaction {
        SchemaUtils.createMissingTablesAndColumns(
            EntityTable,
            SnapshotTable,
            Locks,
            Sessions
        )
    }

    override fun install(route: Route) = Api.install(route)

}