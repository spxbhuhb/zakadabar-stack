/*
 * Copyright © 2020, Simplexion, Hungary
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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