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

/**
 * Use [ScopedViewContract] when the given view may be used more than once on
 * the same frontend AND it is linked to a local data scope.
 *
 * For example: to make it is possible to have two document editors open at the
 * same time in the same window the components used by the editor have to know
 * which editor they belong to. So, they use a scope which in this case is the
 * editor instance itself.
 */
abstract class ScopedViewContract : ViewContract() {

    /**
     * Create a view in the given scope.
     */
    abstract fun newInstance(scope: Any?): Any

    /**
     * Create a new instance as a [ComplexElement] in the given scope.
     */
    fun newElement(scope: Any?) = newInstance(scope) as ComplexElement

    /**
     * By default scoped views do not to support un-scoped use, so
     * [newInstance] without a scope throws NotImplementedError.
     */
    override fun newInstance(): Any {
        throw NotImplementedError()
    }

}