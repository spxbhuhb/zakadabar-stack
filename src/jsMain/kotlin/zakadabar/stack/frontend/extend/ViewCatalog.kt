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

package zakadabar.stack.frontend.extend

import zakadabar.stack.util.UUID
import kotlin.math.max
import kotlin.math.min

/**
 * A catalog of user interface views. Extensions install components into
 * this catalog and the priority they've been installed with decides which
 * component will actually be used by the interface.
 */
open class ViewCatalog {

    private val contracts = mutableMapOf<UUID, MutableList<ViewContract>>()

    val size: Int
        get() = contracts.size

    operator fun plusAssign(contract: ViewContract) {

        val list = contracts.getOrPut(contract.target) { mutableListOf() }

        if (contract.uuid == UUID.NIL) {
            list += contract
            return
        }

        val old = list.firstOrNull { it.uuid == contract.uuid }

        if (old == null || old.target != contract.target) {
            list += contract
            return
        }

        old.priority = max(old.priority, contract.priority)
        old.position = min(old.position, contract.position)
    }

    fun getTop(target: UUID, test: ((ViewContract) -> Boolean)? = null): ViewContract? {
        val list = contracts[target] ?: return null

        return if (test != null) {
            list.filter { it.target == target && test(it) }.maxByOrNull { it.priority }
        } else {
            list.filter { it.target == target }.maxByOrNull { it.priority }
        }
    }

    fun getAll(target: UUID, test: ((ViewContract) -> Boolean)? = null): List<ViewContract> {
        val list = contracts[target] ?: return emptyList()

        return if (test != null) {
            list.filter { it.target == target && test(it) }
        } else {
            list.filter { it.target == target }
        }
    }
}