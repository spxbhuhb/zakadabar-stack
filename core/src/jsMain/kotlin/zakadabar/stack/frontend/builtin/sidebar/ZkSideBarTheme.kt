/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

data class ZkSideBarTheme(
    val background: String = "inherit",
    val border: String,
    val text: String = "inherit",
    val hoverBackground: String = "rgba(255,255,255,0.2)", // FIXME merge this with color.hoverBackground
    val hoverText: String = text, // FIXME merge this with color.hoverForeground
    val activeBackground: String = background,
    val activeText: String = text,
)