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

package zakadabar.stack.frontend.util

fun svg(icon: String, size: Int) =
    """<svg xmlns="http://www.w3.org/2000/svg" width="$size" height="$size" viewBox="0 0 32 32">${icon}</svg>"""

fun svg(icon: String, width: Int, height: Int) =
    """<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height" viewBox="0 0 32 32">${icon}</svg>"""

fun svg(icon: String, className: String, size: Int) =
    """<svg xmlns="http://www.w3.org/2000/svg" width="$size" height="$size" viewBox="0 0 32 32" class="$className">${icon}</svg>"""

fun svg(icon: String, className: String, width: Int, height: Int) =
    """<svg xmlns="http://www.w3.org/2000/svg" width="$width" height="$height" viewBox="0 0 32 32" class="$className">${icon}</svg>"""