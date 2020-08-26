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
package zakadabar.stack.frontend.builtin.util

import zakadabar.stack.frontend.elements.CoreClasses.Companion.coreClasses
import zakadabar.stack.frontend.elements.SimpleElement

class Initials(displayName: String) : SimpleElement() {

    init {
        innerText = if (displayName.length < 2) {
            displayName.toUpperCase()
        } else {
            val e = displayName.split(" ")
            if (e.size < 2) {
                displayName.substring(0, 2).toUpperCase()
            } else {
                "${e[0][0].toUpperCase()}${e[1][0].toUpperCase()}"
            }
        }

        className = coreClasses.avatar
    }

}
