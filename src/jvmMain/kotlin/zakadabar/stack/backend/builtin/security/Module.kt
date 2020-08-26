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

package zakadabar.stack.backend.builtin.security

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import zakadabar.stack.backend.builtin.security.data.AclEntryTable
import zakadabar.stack.backend.builtin.security.data.RoleGrantTable
import zakadabar.stack.backend.extend.BackendModule
import zakadabar.stack.util.UUID

object Module : BackendModule() {

    override val uuid = UUID("00dfb09c-0a85-4f11-a8cf-b06b5f1d78dd")

    override fun init() = transaction {
        SchemaUtils.createMissingTablesAndColumns(
            AclEntryTable,
            RoleGrantTable
        )
    }

}