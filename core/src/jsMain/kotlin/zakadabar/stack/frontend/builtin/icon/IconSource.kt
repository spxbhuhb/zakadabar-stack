/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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