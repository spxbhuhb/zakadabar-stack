/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom.model

import zakadabar.lib.xlsx.dom.Part
import zakadabar.lib.xlsx.dom.SimpleDomElement

internal class WorkBook : SimpleDomElement("workbook"), Part {

    override val partName = "/xl/workbook.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument"

    val sheets = of("sheets")

    init {
        attributes["xmlns"] = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"
        attributes["xmlns:r"] = "http://schemas.openxmlformats.org/officeDocument/2006/relationships"

        childNodes += sheets

    }

    fun nextSheetId() = sheets.childNodes.size + 1

    fun addSheet(sheetId : Int, relId: String, title: String) {

        sheets.childNodes += of("sheet" ,
            "name" to title,
            "sheetId" to sheetId.toString(),
            "r:id" to relId
        )

    }

}

