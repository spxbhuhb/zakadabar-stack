/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.browser

import fillRow
import kotlinx.datetime.Clock
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import save
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.util.downloadBlob
import zakadabar.core.data.BaseBo
import zakadabar.lib.xlsx.buildFileContent
import zakadabar.lib.xlsx.conf.XlsxConfiguration
import zakadabar.lib.xlsx.model.XlsxDocument

private const val XLSX_CONTENT_TYPE  = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

fun <T: BaseBo> ZkTable<T>.onExportXlsx() {
    val xlsxFileName = exportFileName.replace(".csv", ".xlsx")
    val doc = toXlsxDocument()
    doc.save(xlsxFileName)
}

fun <T: BaseBo> ZkTable<T>.toXlsxDocument(
    title: String  = titleText ?: "Export data",
    config: XlsxConfiguration = XlsxConfiguration()
) : XlsxDocument {

    val doc = XlsxDocument(config)
    val sheet = doc.newSheet(title)

    val data = if (exportFiltered) filteredData else fullData
    val columns = columns.filter { it.exportable }

    var rowOffset = 1

    if (exportHeaders) {
        val header = columns.map { it.exportCsvHeader() }
        sheet.fillRow("A1", header)
        rowOffset++
    }

    data.forEachIndexed { ri, row ->
        columns.forEachIndexed { ci, column ->
            val cell = sheet[1 + ci, rowOffset + ri]
            cell.value = column.exportRaw(row.data)
        }
    }

    return doc
}

fun downloadXlsX(fileName: String, doc: XlsxDocument) {
    console.log("${Clock.System.now()} $fileName download triggered")
    doc.buildFileContent {
        val blob = Blob(arrayOf(this), BlobPropertyBag(XLSX_CONTENT_TYPE))
        downloadBlob(fileName, blob)
        console.log("${Clock.System.now()} $fileName download completed")
    }
}
