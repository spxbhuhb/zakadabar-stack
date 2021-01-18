/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

class IconSource(val content: String) {

    private val svg16 = lazy {
        """<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24">$content</svg>"""
    }

    val svg18 = lazy {
        """<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24">$content</svg>"""
    }

    private val svg20 = lazy {
        """<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24">$content</svg>"""
    }

    fun svg(size: Int) = """<svg xmlns="http://www.w3.org/2000/svg" width="$size" height="$size" viewBox="0 0 24 24">$content</svg>"""


    val simple18
        get() = SimpleIcon(svg18.value)

    fun complex16(onClick: (() -> Unit)? = null) = ComplexIcon(svg16.value, onClick)

    fun complex20(onClick: (() -> Unit)? = null) = ComplexIcon(svg20.value, onClick)

    fun simple(viewBox: Int) =
        SimpleIcon("""<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 $viewBox $viewBox">$content</svg>""")

}