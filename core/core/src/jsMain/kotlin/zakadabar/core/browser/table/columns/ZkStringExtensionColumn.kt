/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.input.ZkTextInput
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo

/**
 * A column that contains data that is independent of the BO presented
 * by the table.
 */
class ZkStringExtensionColumn<T : BaseBo>(
    table: ZkTable<T>,
    override var label: String,
    val onChange: ((T, String) -> Unit)? = null
) : ZkColumn<T>(table) {

    val values = mutableMapOf<String, String>()

    override fun onTableSetData() {
        values.clear()
    }

    override fun render(cell: ZkElement, index: Int, row: T) {
        with(cell) {
            + table.styles.dense
            + ZkTextInput(value = values[table.getRowId(row)] ?: "") {
                values[table.getRowId(row)] = it
                onChange?.invoke(row, it)
            }
        }
    }

    override fun sort() {
        table.fullData = if (sortAscending) {
            table.fullData.sortedBy { values[table.getRowId(it.data)] ?: "" }
        } else {
            table.fullData.sortedByDescending { values[table.getRowId(it.data)] ?: "" }
        }
    }

}