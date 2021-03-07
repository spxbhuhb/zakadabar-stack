/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.dock

import zakadabar.stack.frontend.resources.ZkColors

class ZkDockTheme(
    var background: String = ZkColors.white,
    var headerBackground: String = ZkColors.BlueGray.c600,
    var headerForeground: String = ZkColors.black,
    var headerIconBackground: String = "transparent",
    var headerIconFill: String = ZkColors.white,
    var headerHeight: String = "26px"
)