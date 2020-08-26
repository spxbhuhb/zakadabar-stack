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

import zakadabar.stack.data.entity.EntityDto
import zakadabar.stack.frontend.builtin.icon.Icons

/**
 * A contract for entity builders.
 */
abstract class NewEntityContract : ScopedViewContract() {

    open fun supportsParent(dto: EntityDto?) = true

    abstract val name: String

    open val icon = Icons.description.simple18

}