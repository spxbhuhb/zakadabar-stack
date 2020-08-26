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

package zakadabar.stack.backend.builtin.entities.data

import org.jetbrains.exposed.dao.id.LongIdTable
import zakadabar.stack.Stack

object Locks : LongIdTable("t_${Stack.shid}_locks") {
    val entity = reference("entity", EntityTable).index()
    val session = reference("session", Sessions)
    val revision = long("revision")
    val content = long("content")
}

