/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.browser

import buildFileContent
import fillRow
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.util.downloadBlob
import zakadabar.core.data.BaseBo
import zakadabar.lib.xlsx.conf.XlsxConfiguration
import zakadabar.lib.xlsx.model.XlsxDocument

fun <T: BaseBo> ZkTable<T>.onExportXlsx(
    title: String = titleText ?: "Export data",
    config: XlsxConfiguration = XlsxConfiguration()
) {

    val doc = toXlsxDocument(title, config)

    doc.buildFileContent {
        val xlsxFileName = exportFileName.replace(".csv", ".xlsx")
        val blob = toBlob("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        downloadBlob(xlsxFileName, blob)
    }

}

fun <T: BaseBo> ZkTable<T>.toXlsxDocument(title: String, config: XlsxConfiguration) : XlsxDocument {

    val doc = XlsxDocument(config)
    val sheet = doc[title]

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

private fun ByteArray.toBlob(contentType: String) : Blob = Blob(arrayOf(this), BlobPropertyBag(type = contentType))

