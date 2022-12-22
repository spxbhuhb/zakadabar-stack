/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.browser

import setRow
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.model.XlsxSheet
import zakadabar.lib.xlsx.saveXlsx
import zakadabar.lib.xlsx.toContentMap

fun <T: BaseBo> ZkTable<T>.onExportXlsx(title: String = titleText ?: "Export data") {

    val xlsxFileName = exportFileName.replace(".csv", ".xlsx")

    val xlsx = XlsxDocument()
    val sheet = XlsxSheet(title)
    xlsx += sheet

    val data = if (exportFiltered) filteredData else fullData
    val columns = columns.filter { it.exportable }

    var rowIndex = 0

    if (exportHeaders) {
        val header = columns.map { it.exportCsvHeader() }
        sheet.setRow("A1", header)
        rowIndex++
    }

    data.forEach { row ->
        columns.forEachIndexed { colIndex, zkColumn ->
            val cell = sheet[1 + colIndex, 1 + rowIndex]
            cell.value = zkColumn.exportCsv(row.data)
        }
        rowIndex++
    }

    val content = xlsx.toContentMap()
    content.saveXlsx(xlsxFileName)

}
