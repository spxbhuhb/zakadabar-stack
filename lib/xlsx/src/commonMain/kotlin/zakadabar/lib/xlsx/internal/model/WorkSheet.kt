/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.internal.dom.Node

internal class WorkSheet(sheetId: Int) : Node("worksheet"), Part {

    override val partName = "/xl/worksheets/sheet$sheetId.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet"

    private val dimension = + Node("dimension")
    private val cols = + Node("cols")
    private val sheetData = + Node("sheetData")

    init {
        this["xmlns"] = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"
    }

    fun addRow(row: Row) {
        sheetData += row
    }

    fun addColumnWidth(colNumber: Int, width: Double) {
        cols += Node("col") {
            this["min"] = colNumber.toString()
            this["max"] = colNumber.toString()
            this["width"] = width.toString()
            this["customWidth"] = "1"
        }
    }

    /**
     * add dimension tag to document
     */
    fun addDimension(ref: String) {
        dimension["ref"] = ref
    }

    /**
     * remove non-used, empty tags
     */
    fun clean() {
        if (dimension.hasNoAttribute()) this -= dimension
        if (cols.isEmpty()) this -= cols
    }

}

