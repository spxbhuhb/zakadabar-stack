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

import zakadabar.stack.frontend.elements.ComplexElement
import zakadabar.stack.util.UUID

abstract class ViewContract {

    open val uuid = UUID.NIL

    open val target = UUID.NIL

    var installOrder = 0
    open var priority = 0
    open var position = 0

    abstract fun newInstance(): Any

    fun newElement() = newInstance() as ComplexElement

    override fun toString() =
        "${this::class.simpleName}(self=$uuid, target=$target, priority=$priority, position=$position, installOrder=$installOrder)"

}