/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.browser

import fillRow
import kotlinx.datetime.Clock
import org.khronos.webgl.Uint8Array
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag
import save
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.browser.util.downloadBlob
import zakadabar.core.data.BaseBo
import zakadabar.lib.xlsx.internal.buildFileContent
import zakadabar.lib.xlsx.conf.XlsxConfiguration
import zakadabar.lib.xlsx.internal.ContentWriter
import zakadabar.lib.xlsx.model.XlsxDocument

/*
    Extension and help methods for Browser
 */

/**
 * xlsx content type for downloading
 */
private const val XLSX_CONTENT_TYPE  = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

/**
 * export and download ZkTable data, with default Configuration
 */
fun <T: BaseBo> ZkTable<T>.onExportXlsx() {
    val doc = toXlsxDocument("Data export", XlsxConfiguration())
    doc.save(exportXlsxFileName)
}

/**
 * Copy ZkTable data to XlsxDocument, according to exportFiltered and exportHeaders ZkTable properties
 *
 * Typed cell value is acquired by ZkColumn.exportRaw method.
 *
 * Starts at the left-top corner (A1)
 */
fun <T: BaseBo> ZkTable<T>.toXlsxDocument(title: String, config: XlsxConfiguration) : XlsxDocument {

    val doc = XlsxDocument(config)
    val sheet = doc.newSheet(title)

    val data = if (exportFiltered) filteredData else fullData
    val columns = columns.filter { it.exportable }

    var rowIndex = 1

    if (exportHeaders) {
        val header = columns.map { it.exportCsvHeader() }
        sheet.fillRow("A1", header)
        rowIndex++
    }

    for(row in data) {
        var columnIndex = 1
        for(column in columns) {
            val value = column.exportRaw(row.data)
            val cell = sheet[columnIndex, rowIndex]
            cell.value = value
            columnIndex++
        }
        rowIndex++
    }

    return doc
}

/**
 * Filename, derived from exportFileName
 */
val <T: BaseBo> ZkTable<T>.exportXlsxFileName : String
    get() = exportFileName.replace(".csv", ".xlsx")

/**
 * Browser helper method to download an XlsxDocument via javascript Blob object
 */
fun downloadXlsX(fileName: String, doc: XlsxDocument) {

    console.log("${Clock.System.now()} $fileName download triggered")

    val blobParts = mutableListOf<Uint8Array>()

    doc.buildFileContent(ContentWriter(blobParts::add) {
        val blob = Blob(blobParts.toTypedArray(), BlobPropertyBag(XLSX_CONTENT_TYPE))
        downloadBlob(fileName, blob)
        console.log("${Clock.System.now()} $fileName download completed, size: ${blob.size}")
    })

}
