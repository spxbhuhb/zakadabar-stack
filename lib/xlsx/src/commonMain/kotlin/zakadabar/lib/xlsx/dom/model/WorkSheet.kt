/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom.model

import zakadabar.lib.xlsx.dom.Part
import zakadabar.lib.xlsx.dom.SimpleDomElement

internal class WorkSheet(sheetId: Int) : SimpleDomElement("worksheet"), Part {

    override val partName = "/xl/worksheets/sheet$sheetId.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet"

    private val sheetData = of("sheetData")

    init {
        attributes["xmlns"] = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"

        childNodes += sheetData

    }

    fun addRow(row: Row) {
        sheetData += row
    }

}

