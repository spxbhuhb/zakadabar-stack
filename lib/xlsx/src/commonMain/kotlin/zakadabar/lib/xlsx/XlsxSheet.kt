/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

data class XlsxSheet(val title : String) {

    private val _rows = mutableMapOf<Int, XlsxRow>()

    val rows : List<XlsxRow> get() = _rows.values.sorted()

    operator fun plusAssign(cell: XlsxCell) {
        val rowNumber = cell.coordinate.rowNumber
        _rows.getOrPut(rowNumber) { XlsxRow(rowNumber) } += cell
    }

}