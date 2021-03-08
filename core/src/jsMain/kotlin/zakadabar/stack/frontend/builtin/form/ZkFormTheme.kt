/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.form

import zakadabar.stack.frontend.resources.ZkColors

class ZkFormTheme(
    val rowHeight: Int = 38,
    val disabledBackground: String = ZkColors.BlueGray.c50,
    val labelBackground: String = "#fafafa",
    val valueBackground: String = ZkColors.white,
    val invalidBackground: String = ZkColors.Red.c100
)