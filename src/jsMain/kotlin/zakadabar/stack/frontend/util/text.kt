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

import kotlinx.browser.document
import org.w3c.dom.HTMLElement

fun escape(source: String) = source.replace("&", "&amp;").replace("<", "&lt;")

fun makeString(source: List<Int>): String {
    // TODO if it is possible, change the toIntArray to something better
    return js("String.fromCodePoint").apply(null, source.toIntArray()) as String
}

/**
 * Pops a file download for the user, the file content will be the text passed.
 */
fun download(filename: String, text: String) {
    val element = document.createElement("A") as HTMLElement
    element.setAttribute("href", "data:text/plain;charset=utf-8," + js("encodeURIComponent")(text) as String)
    element.setAttribute("download", filename)

    element.style.display = "none"
    document.body?.appendChild(element) ?: throw IllegalStateException("document body is null")

    element.click()

    document.body !!.removeChild(element);
}