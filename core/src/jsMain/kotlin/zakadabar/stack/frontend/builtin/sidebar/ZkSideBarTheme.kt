/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

data class ZkSideBarTheme(
    val background: String = "inherit",
    val border: String,
    val text: String = "inherit",
    val hoverBackground: String = "rgba(255,255,255,0.2)",
    val hoverText: String = text,
    val activeBackground: String = background,
    val activeText: String = text,
)