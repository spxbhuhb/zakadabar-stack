/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom.model

import zakadabar.lib.xlsx.dom.Part
import zakadabar.lib.xlsx.dom.SimpleDomElement

internal class Styles : SimpleDomElement("styleSheet"), Part {

    override val partName = "/xl/styles.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"

    val numFmts = of("numFmts").also { childNodes += it }
    val fonts = of("fonts").also { childNodes += it }
    val fills = of("fills").also { childNodes += it }
    val borders = of("borders").also { childNodes += it }
    val cellStyleXfs = of("cellStyleXfs").also { childNodes += it }
    val cellXfs = of("cellXfs").also { childNodes += it }
    val cellStyles = of("cellStyles").also { childNodes += it }

    init {
        attributes["xmlns"] = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"

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
        numFmts += of("numFmt", "numFmtId" to numFmtId, "formatCode" to formatCode)
        return adjustCount(numFmts) - 1
    }

    fun addFont(sz: String, colorTheme: String, name: String, family: String, scheme: String) : Int {
        fonts += of("font").also {
            it += of("sz", "val" to sz)
            it += of("color", "theme" to colorTheme)
            it += of("name", "val" to name)
            it += of("family", "val" to family)
            it += of("scheme", "val" to scheme)
        }
        return adjustCount(fonts) - 1
    }

    fun addPatternFill(patternType: String) : Int {
        fills += of("fill").also {
            it += of("patternFill", "patternType" to patternType)
        }
        return adjustCount(fills) - 1
    }

    fun addBorder() : Int {
        borders += of("border").also {
            it += of("left")
            it += of("right")
            it += of("top")
            it += of("bottom")
            it += of("diagonal")
        }
        return adjustCount(borders) -1
    }

    fun addCellStyleXf(numFmtId: String) : Int = addXf(cellStyleXfs, numFmtId)
    fun addCellXf(numFmtId: String) : Int = addXf(cellXfs, numFmtId)

    fun addCellStyle(name: String, xfId: String, builtinId: String) : Int {
        cellStyles += of("cellStyle", "name" to name, "xfId" to xfId, "builtinId" to builtinId)
        return adjustCount(cellStyles) - 1
    }

    fun addCustomNumFmt(formatCode: String) : Int {
        val numFmtId = (CUSTOM_NUM_FORMAT_ID_BASE + numFmts.childNodes.size).toString()
        addNumFmt(numFmtId, formatCode)
        return addCellXf(numFmtId)
    }

    private fun adjustCount(e: SimpleDomElement) : Int {
        val count = e.childNodes.size
        e.attributes["count"] = count.toString()
        return count
    }

    private fun addXf(xfs: SimpleDomElement, numFmtId: String) : Int {
        xfs += of("xf", "numFmtId" to numFmtId)
        return adjustCount(xfs) - 1
    }

    companion object {
        private const val CUSTOM_NUM_FORMAT_ID_BASE = 164
    }

}

