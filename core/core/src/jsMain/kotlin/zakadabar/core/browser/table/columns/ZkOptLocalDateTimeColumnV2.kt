/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import kotlinx.datetime.LocalDateTime
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.localized

open class ZkOptLocalDateTimeColumnV2<T : BaseBo>(
    table: ZkTable<T>,
    val getter: (T) -> LocalDateTime?
) : ZkColumn<T>(table) {

    override fun render(cell: ZkElement, index: Int, row: T) {
        with(cell) {
            + format(row)
        }
    }

    override fun sort() {
        table.sort(sortAscending) { getter(it.data) }
    }

    override fun matches(row: T, string: String?): Boolean {
        if (string == null) return false
        return (string in format(row).lowercase())
    }

    override fun exportCsv(row: T): String {
        return getter(row)?.localized ?: return ""
    }

    open fun format(row: T): String {
        return getter(row)?.localized ?: return ""
    }
}