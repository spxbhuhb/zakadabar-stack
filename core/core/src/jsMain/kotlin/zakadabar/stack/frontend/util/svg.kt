/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
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