/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.internal.dom.Node

internal class WorkBook : Node("workbook"), Part {

    override val partName = "/xl/workbook.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument"

    val sheets = + Node("sheets")

    init {
        this["xmlns"] = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"
        this["xmlns:r"] = "http://schemas.openxmlformats.org/officeDocument/2006/relationships"
    }

    fun nextSheetId() = sheets.size + 1

    fun addSheet(sheetId : Int, relId: String, name: String) {

        sheets += Node("sheet") {
            this["name"] = name
            this["sheetId"] = sheetId.toString()
            this["r:id"] = relId
        }

    }

}

