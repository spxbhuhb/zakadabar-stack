/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.table.columns

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.input.ZkCheckBox
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo

/**
 * A column that contains data that is independent of the BO presented
 * by the table.
 */
class ZkBooleanExtensionColumn<T : BaseBo>(
    table: ZkTable<T>,
    override var label: String,
    val onChange: ((T, Boolean) -> Unit)? = null
) : ZkColumn<T>(table) {

    val values = mutableMapOf<String, Boolean>()

    override fun onTableSetData() {
        values.clear()
    }

    override fun render(cell: ZkElement, index: Int, row: T) {
        with(cell) {
            + table.styles.dense
            + ZkCheckBox(checked = values[table.getRowId(row)] ?: false) {
                values[table.getRowId(row)] = it
                onChange?.invoke(row, it)
            }
        }
    }

    override fun sort() {
        table.fullData = if (sortAscending) {
            table.fullData.sortedBy { values[table.getRowId(it.data)] ?: false }
        } else {
            table.fullData.sortedByDescending { values[table.getRowId(it.data)] ?: false }
        }
    }

}