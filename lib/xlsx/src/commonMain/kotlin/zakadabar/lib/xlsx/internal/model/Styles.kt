/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.dom.*
import zakadabar.lib.xlsx.internal.putCount

internal class Styles : Node("styleSheet", arrayOf(
    "xmlns" to "http://schemas.openxmlformats.org/spreadsheetml/2006/main"
)), Part {

    override val partName = "/xl/styles.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"

    val numFmts = add("numFmts")
    val fonts = add("fonts")
    val fills = add("fills")
    val borders = add("borders")
    val cellStyleXfs = add("cellStyleXfs")
    val cellXfs = add("cellXfs")
    val cellStyles = add("cellStyles")

    init {

        addFont("11", "1", "Calibri", "2", "minor")
        addPatternFill("none")
        addBorder()
        addCellStyleXf("0")
        addCellXf("0")
        addCellXf("14")
        addCellXf("22")
        addCellStyle("normal", "0", "0")

        addCustomNumFmt("yyyy-mm-dd hh:mm:ss.000")
    }

    fun addNumFmt(numFmtId: String, formatCode: String) : Int {
        numFmts.add("numFmt", "numFmtId" to numFmtId, "formatCode" to formatCode)
        return numFmts.putCount() - 1
    }

    fun addFont(sz: String, colorTheme: String, name: String, family: String, scheme: String) : Int {
        fonts.add("font") {
            add("sz", "val" to sz)
            add("color", "theme" to colorTheme)
            add("name", "val" to name)
            add("family", "val" to family)
            add("scheme", "val" to scheme)
        }
        return fonts.putCount() - 1
    }

    fun addPatternFill(patternType: String) : Int {
        fills.add("fill") {
            add("patternFill", "patternType" to patternType)
        }
        return fills.putCount() - 1
    }

    fun addBorder() : Int {
        borders.add("border") {
            add("left")
            add("right")
            add("top")
            add("bottom")
            add("diagonal")
        }
        return borders.putCount() -1
    }

    fun addCellStyleXf(numFmtId: String) : Int = addXf(cellStyleXfs, numFmtId)
    fun addCellXf(numFmtId: String) : Int = addXf(cellXfs, numFmtId)

    fun addCellStyle(name: String, xfId: String, builtinId: String) : Int {
        cellStyles.add("cellStyle", "name" to name, "xfId" to xfId, "builtinId" to builtinId)
        return cellStyles.putCount() - 1
    }

    fun addCustomNumFmt(formatCode: String) : Int {
        val numFmtId = (CUSTOM_NUM_FORMAT_ID_BASE + numFmts.elements.size).toString()
        addNumFmt(numFmtId, formatCode)
        return addCellXf(numFmtId)
    }

    private fun addXf(xfs: Node, numFmtId: String) : Int {
        xfs.add("xf", "numFmtId" to numFmtId)
        return xfs.putCount() - 1
    }

    companion object {
        private const val CUSTOM_NUM_FORMAT_ID_BASE = 164
    }

}

