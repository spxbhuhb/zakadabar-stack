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

package zakadabar.stack.backend.builtin.security.data

import org.jetbrains.exposed.dao.id.IdTable
import zakadabar.stack.Stack
import zakadabar.stack.backend.builtin.entities.data.EntityTable

object AclEntryTable : IdTable<Long>("t_${Stack.shid}_acl_entries") {
    override val id = reference("id", EntityTable)

    val subject = reference("subject", EntityTable)
    val access = varchar("access", 40)
    val allow = bool("allow")

    override val primaryKey = PrimaryKey(id)
}