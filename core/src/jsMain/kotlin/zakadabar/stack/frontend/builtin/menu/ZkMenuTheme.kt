/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

data class ZkMenuTheme(
    val background: String,
    val text: String,
    val hoverBackground: String,
    val hoverText: String = text,
    val activeBackground: String,
    val activeText: String = text
)