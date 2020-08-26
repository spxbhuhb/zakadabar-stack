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

package zakadabar.stack.util


/**
 * A general catalog of unique elements.
 * The dual-store (map and list) is meant to avoid converting the map into a list again and again.
 */
open class CatalogOfUniques<U : Unique> {

    protected val map = mutableMapOf<UUID, U>()
    protected val list = mutableListOf<U>()

    operator fun plusAssign(item: U) {
        val registered = map[item.uuid]

        if (registered != null) {
            if (registered === item) return
            throw IllegalArgumentException("item ${item.uuid} already registered")
        }

        map[item.uuid] = item
        list += item
    }

    open operator fun get(uuid: UUID) = map[uuid]

    fun forEach(func: (U) -> Unit) = list.forEach(func)

}