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

actual fun fourRandomInt(): IntArray {
    val buffer = IntArray(4)

    // TODO this works properly in browser only, in tests / node should use crypto module
    js(
        """
        if (window && window.crypto) {
            window.crypto.getRandomValues(buffer);
        } else {
            buffer[0] = Math.random() * Number.MAX_SAFE_INTEGER;
            buffer[1] = Math.random() * Number.MAX_SAFE_INTEGER;
            buffer[2] = Math.random() * Number.MAX_SAFE_INTEGER;
            buffer[3] = Math.random() * Number.MAX_SAFE_INTEGER; 
        }
         """
    )

    return buffer
}