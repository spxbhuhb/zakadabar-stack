/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

import zakadabar.core.browser.ZkElement

class ZkTableRow<T>(
    var index : Int,
    var data: T,
) {
    var element: ZkElement? = null
    var height: Double? = null
    var level = 0
    var levelState: ZkRowLevelState = ZkRowLevelState.Single
}