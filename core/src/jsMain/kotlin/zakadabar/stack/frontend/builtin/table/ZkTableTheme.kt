/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.resources.MaterialColors

data class ZkTableTheme(
    val headerBackground: String = MaterialColors.white,
    val headerText: String = MaterialColors.Gray.c800,
    val oddRowBackground: String = MaterialColors.white,
    val evenRowBackground: String = MaterialColors.white,
    val text: String = MaterialColors.BlueGray.c900,
    val hoverBackground: String = MaterialColors.LightBlue.c50,
    val hoverText: String = text,
    val innerBorder: String = MaterialColors.Gray.c300,
    val headerBottom: String = MaterialColors.LightBlue.a700,
    val border: String? = null,
    val actionColor: String? = MaterialColors.LightBlue.a700
)