/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.browser

import setRow
import toContentMap
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.lib.xlsx.conf.XlsxConfiguration
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.saveXlsx

fun <T: BaseBo> ZkTable<T>.onExportXlsx(
    title: String = titleText ?: "Export data",
    config: XlsxConfiguration = XlsxConfiguration()
) {

    val doc = XlsxDocument(config)
    val sheet = doc[title]

    val data = if (exportFiltered) filteredData else fullData
    val columns = columns.filter { it.exportable }

    var rowOffset = 1

    if (exportHeaders) {
        val header = columns.map { it.exportCsvHeader() }
        sheet.setRow("A1", header)
        rowOffset++
    }

    data.forEachIndexed { ri, row ->
        columns.forEachIndexed { ci, column ->
            val cell = sheet[1 + ci, rowOffset + ri]
            cell.value = column.exportRaw(row.data)
        }
    }

    val content = doc.toContentMap()

    val xlsxFileName = exportFileName.replace(".csv", ".xlsx")
    content.saveXlsx(xlsxFileName)

}
