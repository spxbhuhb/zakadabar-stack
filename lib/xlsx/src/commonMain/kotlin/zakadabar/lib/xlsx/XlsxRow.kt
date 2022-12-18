/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

data class XlsxRow(val rowNumber: Int) : Comparable<XlsxRow> {

    private val _cells = mutableMapOf<XlsxCoordinate, XlsxCell>()

    val cells : List<XlsxCell> get() = _cells.values.sorted()

    operator fun plusAssign(cell: XlsxCell) {
        if (rowNumber != cell.coordinate.rowNumber) throw IllegalArgumentException("Row not matched. $rowNumber != ${cell.coordinate.rowNumber}")
        _cells[cell.coordinate] = cell
    }

    override fun compareTo(other: XlsxRow): Int = rowNumber.compareTo(other.rowNumber)

}
