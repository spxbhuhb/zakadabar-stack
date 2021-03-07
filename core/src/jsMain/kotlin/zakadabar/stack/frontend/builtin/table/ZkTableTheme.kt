/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.table

import zakadabar.stack.frontend.resources.ZkColors

data class ZkTableTheme(
    val headerBackground: String = ZkColors.white,
    val headerText: String = ZkColors.Gray.c800,
    val oddRowBackground: String = ZkColors.white,
    val evenRowBackground: String = ZkColors.white,
    val text: String = ZkColors.BlueGray.c900,
    val hoverBackground: String = ZkColors.LightBlue.c50,
    val hoverText: String = text,
    val innerBorder: String = ZkColors.Gray.c300,
    val headerBottom: String = ZkColors.LightBlue.a700,
    val border: String? = null,
    val actionColor: String? = ZkColors.LightBlue.a700
)