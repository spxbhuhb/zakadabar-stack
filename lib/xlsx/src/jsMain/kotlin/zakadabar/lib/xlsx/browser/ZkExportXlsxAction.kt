/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.browser

import setRow
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.data.BaseBo
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.model.XlsxSheet
import zakadabar.lib.xlsx.saveXlsx
import zakadabar.lib.xlsx.toContentMap

open class ZkExportXlsxAction(
    onExecute: () -> Unit
) : ZkButton(ZkIcons.xlsx, ZkFlavour.Primary, buttonSize = 24, iconSize = 18, round = true, onClick = onExecute)


fun <T: BaseBo> ZkTable<T>.onExportXlsx() {

    val xlsxFileName = exportFileName.replace(".csv", ".xlsx")

    val xlsx = XlsxDocument()
    val sheet = XlsxSheet(xlsxFileName)
    xlsx += sheet

    val data = if (exportFiltered) filteredData else fullData
    val columns = columns.filter { it.exportable }

    if (exportHeaders) {
        val header = columns.map { it.exportCsvHeader() }
        sheet.setRow("A1", header)
    }

    var rowIndex = 0

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



