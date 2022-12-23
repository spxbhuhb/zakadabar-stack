/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.conf.XlsxNumberFormat
import zakadabar.lib.xlsx.dom.*
import zakadabar.lib.xlsx.internal.putCount

internal class Styles : Node("styleSheet",
"xmlns" to "http://schemas.openxmlformats.org/spreadsheetml/2006/main"
), Part {

    override val partName = "/xl/styles.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"

    val numFmts = + Node("numFmts")
    val fonts = + Node("fonts")
    val fills = + Node("fills")
    val borders = + Node("borders")
    val cellStyleXfs = + Node("cellStyleXfs")
    val cellXfs = + Node("cellXfs")
    val cellStyles = + Node("cellStyles")

    init {

        addFont("11", "1", "Calibri", "2", "minor")
        addPatternFill("none")
        addBorder()
        addCellStyleXf(0)
        addCellStyle("normal", "0", "0")
        addNumberFormats()

    }



    fun addNumFmt(numFmtId: Int, formatCode: String) : Int {
        numFmts += Node("numFmt", "numFmtId" to numFmtId.toString(), "formatCode" to formatCode)
        return numFmts.putCount() - 1
    }

    fun addFont(sz: String, colorTheme: String, name: String, family: String, scheme: String) : Int {
        fonts += Node("font") {
            + Node("sz", "val" to sz)
            + Node("color", "theme" to colorTheme)
            + Node("name", "val" to name)
            + Node("family", "val" to family)
            + Node("scheme", "val" to scheme)
        }
        return fonts.putCount() - 1
    }

    fun addPatternFill(patternType: String) : Int {
        fills += Node("fill") {
            + Node("patternFill", "patternType" to patternType)
        }
        return fills.putCount() - 1
    }

    fun addBorder() : Int {
        borders += Node("border") {
            + Node("left")
            + Node("right")
            + Node("top")
            + Node("bottom")
            + Node("diagonal")
        }
        return borders.putCount() -1
    }

    fun addCellStyleXf(numFmtId: Int) : Int = addXf(cellStyleXfs, numFmtId)
    fun addCellXf(numFmtId: Int) : Int = addXf(cellXfs, numFmtId)

    fun addCellStyle(name: String, xfId: String, builtinId: String) : Int {
        cellStyles += Node("cellStyle", "name" to name, "xfId" to xfId, "builtinId" to builtinId)
        return cellStyles.putCount() - 1
    }

    fun addCustomNumFmt(formatCode: String) : Int {
        val numFmtId = CUSTOM_NUM_FORMAT_ID_BASE + numFmts.childNodes.size
        addNumFmt(numFmtId, formatCode)
        return addCellXf(numFmtId)
    }

    private fun addXf(xfs: Node, numFmtId: Int) : Int {
        xfs += Node("xf", "numFmtId" to numFmtId.toString())
        return xfs.putCount() - 1
    }

    private fun addNumberFormats() {
        XlsxNumberFormat.values().forEach {
            it.numFmtId?.let(::addCellXf)
            it.formatCode?.let(::addCustomNumFmt)
        }
    }

    companion object {
        private const val CUSTOM_NUM_FORMAT_ID_BASE = 164
    }

}

