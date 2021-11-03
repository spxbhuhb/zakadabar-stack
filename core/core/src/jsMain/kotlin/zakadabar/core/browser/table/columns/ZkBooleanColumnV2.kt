/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.css.em

open class ZkBooleanColumnV2<T : BaseBo>(
    table: ZkTable<T>,
    val getter: (T) -> Boolean
) : ZkColumn<T>(table) {

    override var max = 3.em

    override fun render(cell: ZkElement, index: Int, row: T) {
        with(cell) {
            + div {
                buildPoint.innerHTML = if (getter(row)) ZkIcons.check.svg(18) else ZkIcons.close.svg(18)
            }
        }
    }

    override fun sort() {
        table.fullData = if (sortAscending) {
            table.fullData.sortedBy { getter(it.data) }
        } else {
            table.fullData.sortedByDescending { getter(it.data) }
        }
    }

}