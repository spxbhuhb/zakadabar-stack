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
package zakadabar.stack.frontend.builtin.icon

class IconSource(val content: String) {

    private val svg16 =
        """<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24">$content</svg>"""
    private val svg18 =
        """<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24">$content</svg>"""
    private val svg20 =
        """<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24">$content</svg>"""

    val simple16
        get() = SimpleIcon(svg16)

    val simple18
        get() = SimpleIcon(svg18)

    val simple20
        get() = SimpleIcon(svg20)

    val complex16
        get() = ComplexIcon(svg16)

    val complex18
        get() = ComplexIcon(svg18)

    val complex20
        get() = ComplexIcon(svg16)

    fun complex16(onClick: (() -> Unit)? = null) = ComplexIcon(svg16, onClick)

    fun complex18(onClick: (() -> Unit)? = null) = ComplexIcon(svg18, onClick)

    fun complex20(onClick: (() -> Unit)? = null) = ComplexIcon(svg20, onClick)

    fun simple(size: Int) =
        SimpleIcon("""<svg xmlns="http://www.w3.org/2000/svg" width="$size" height="$size" viewBox="0 0 24 24">$content</svg>""")

    fun complex(size: Int, onClick: (() -> Unit)? = null) =
        ComplexIcon(
            """<svg xmlns="http://www.w3.org/2000/svg" width="$size" height="$size" viewBox="0 0 24 24">$content</svg>""",
            onClick
        )


}