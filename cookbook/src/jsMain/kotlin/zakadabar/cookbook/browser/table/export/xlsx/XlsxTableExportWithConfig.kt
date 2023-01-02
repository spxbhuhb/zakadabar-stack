/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.export.xlsx

import save
import zakadabar.cookbook.browser.table.demoData
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.browser.table.ZkTable
import zakadabar.core.resource.css.em
import zakadabar.lib.xlsx.browser.exportXlsxFileName
import zakadabar.lib.xlsx.browser.toXlsxDocument
import zakadabar.lib.xlsx.conf.XlsxConfiguration

class XlsxTableExportWithConfig :  ZkTable<ExampleBo>() {

    override fun onConfigure() {
        super.onConfigure()

        addLocalTitle = true
        export = true
        exportHeaders = true

        + ExampleBo::stringValue size 5.em
        + ExampleBo::booleanValue size 2.2.em
        + ExampleBo::doubleValue size 4.em
        + ExampleBo::localDateValue size 6.6.em
        + ExampleBo::localDateTimeValue size 10.1.em
        + ExampleBo::instantValue size 10.1.em
    }

    override fun onCreate() {
        super.onCreate()
        demoData(500)
    }

    override fun getRowId(row: ExampleBo) =
        row.id.toString()

    override fun onDblClick(id: String) = Unit

    override fun onExportCsv() = onExportXlsx()

    fun onExportXlsx() {
        val cfg = XlsxConfiguration()

        val secundumPrecisionDateFormat = cfg.formats.ISO_DATETIME_SEC
        cfg.dateTimeFormat = secundumPrecisionDateFormat

        val customDecimalFormat = cfg.formats.CustomNumberFormat("#,##0.00")

        val sheetName = "Daily report"

        // generate xlsx model
        val doc = toXlsxDocument(sheetName, cfg)

        // acquire the sheet
        val sheet = doc.sheets.first()

        // adjust column width
        sheet.columns["E"].width = 20.0
        sheet.columns["F"].width = 23.0

        // set the format in a column, except the header roe
        for(row in 2..sheet.maxRowNumber) {
            sheet[3, row].numberFormat = customDecimalFormat
        }

        // create another sheet
        val sheet2 = doc.newSheet("Summary")
        sheet2["A1"].value = "total"
        sheet2["B1"].value = 42

        // download
        doc.save(exportXlsxFileName)

    }

}