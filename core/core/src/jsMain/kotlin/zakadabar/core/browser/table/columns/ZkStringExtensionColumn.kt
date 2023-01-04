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
        table.sort(sortAscending) { values[table.getRowId(it.data)] ?: "" }
    }

    override fun exportCsv(row: T): String =
        values[table.getRowId(row)].let {
            it ?: ""
        }

    override fun exportRaw(row: T): Any? = values[table.getRowId(row)]

}