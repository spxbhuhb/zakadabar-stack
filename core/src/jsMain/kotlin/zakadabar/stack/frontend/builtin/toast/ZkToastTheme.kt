/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.toast

import zakadabar.stack.frontend.resources.ZkColors

class ZkToastTheme(
    val infoBackground: String = "#81d4fa",
    val infoText: String = ZkColors.black,
    val successBackground: String = "#087f23",
    val successText: String = ZkColors.white,
    val warningBackground: String = "#ffea00",
    val warningText: String = ZkColors.black,
    val errorBackground: String = "#c41c00",
    val errorText: String = ZkColors.white
)