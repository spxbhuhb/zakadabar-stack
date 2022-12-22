/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.dom.*

internal class WorkBook : Node("workbook", arrayOf(
    "xmlns" to "http://schemas.openxmlformats.org/spreadsheetml/2006/main",
    "xmlns:r" to "http://schemas.openxmlformats.org/officeDocument/2006/relationships"
)), Part {

    override val partName = "/xl/workbook.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument"

    val sheets = add("sheets")

    fun nextSheetId() = sheets.elements.size + 1

    fun addSheet(sheetId : Int, relId: String, title: String) {

        sheets.add("sheet" ,
            "name" to title,
            "sheetId" to sheetId.toString(),
            "r:id" to relId
        )

    }

}

