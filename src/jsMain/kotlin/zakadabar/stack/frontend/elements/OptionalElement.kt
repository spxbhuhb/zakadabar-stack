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

package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses

/**
 * An element to use in customizable UI classes when there is an optional
 * element subclasses may want to implement. For example an optional
 * header or an optional icon.
 *
 * This element simply hides itself, so it won't be shown if the subclasses
 * does not replace it with something else.
 */
open class OptionalElement : SimpleElement() {

    init {
        element.className = coreClasses.hidden
    }

}