/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.util.UUID

open class ZkOptUuidColumnV2<T : BaseBo>(
    table: ZkTable<T>,
    val getter: (T) -> UUID?
) : ZkColumn<T>(table) {

    override fun render(cell: ZkElement, index: Int, row: T) {
        with(cell) {
            + getter(row)?.toString() ?: ""
        }
    }

    override fun sort() {
        table.fullData = if (sortAscending) {
            table.fullData.sortedBy { getter(it.data) }
        } else {
            table.fullData.sortedByDescending { getter(it.data) }
        }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return (getter(row)?.toString()?.lowercase()?.contains(string)) ?: false
    }

    override fun exportCsv(row: T): String {
        return getter(row)?.toString() ?: ""
    }

}