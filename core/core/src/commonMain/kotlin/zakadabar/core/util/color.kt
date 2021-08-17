/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.util

fun String.alpha(value: Double): String {
    if (startsWith("#") && length == 7) {
        val hex = (value * 255).toInt().toString(16)
        return this + (if (hex.length == 1) "0$hex" else hex)
    }
    TODO("color alpha conversion only handles #RRGGBB format at the moment")
}