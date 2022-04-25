/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table

import zakadabar.core.browser.ZkElement

/**
 * State of one table row.
 *
 * @param     index        The index of the row in [ZkTable.fullData].
 * @param     data         Data of the row.
 * @property  element      The `tr` element added to the table.
 * @property  height       Height of the row in pixels, calculated after it is added to the DOM.
 * @property  level        Level of the row for multi-level tables.
 * @property  levelState   State of the row for multi-level tables.
 */
class ZkTableRow<T>(
    var index : Int,
    var data: T,
) {
    var element: ZkElement? = null
    var height: Double? = null
    var level = 0
    var levelState: ZkRowLevelState = ZkRowLevelState.Single
}