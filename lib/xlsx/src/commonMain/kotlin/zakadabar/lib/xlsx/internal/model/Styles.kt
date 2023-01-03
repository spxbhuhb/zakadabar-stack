/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

import zakadabar.lib.xlsx.internal.dom.Node
import zakadabar.lib.xlsx.internal.putCount

internal class Styles : Node("styleSheet"), Part {

    override val partName = "/xl/styles.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"

    val numFmts = + node("numFmts")
    val fonts = + node("fonts")
    val fills = + node("fills")
    val borders = + node("borders")
    val cellStyleXfs = + node("cellStyleXfs")
    val cellXfs = + node("cellXfs")
    val cellStyles = + node("cellStyles")

    init {
        this["xmlns"] =  "http://schemas.openxmlformats.org/spreadsheetml/2006/main"

        addFont("11", "1", "Calibri", "2", "minor")
        addPatternFill("none")
        addBorder()
        addCellStyleXf(0)
        addCellStyle("normal", "0", "0")

    }

    fun addNumFmt(numFmtId: Int, formatCode: String) : Int {
        numFmts += node("numFmt") {
            this["numFmtId"] = numFmtId.toString()
            this["formatCode"] = formatCode
        }
        return numFmts.putCount() - 1
    }

    fun addFont(sz: String, colorTheme: String, name: String, family: String, scheme: String) : Int {
        fonts += node("font") {
            + node("sz") {
                this["val"] = sz
            }
            + node("color") {
                this["theme"] = colorTheme
            }
            + node("name") {
                this["val"] =  name
            }
            + node("family") {
                this["val"] = family
            }
            + node("scheme") {
                this["val"] = scheme
            }
        }
        return fonts.putCount() - 1
    }

    fun addPatternFill(patternType: String) : Int {
        fills += node("fill") {
            + node("patternFill") {
                this["patternType"] = patternType
            }
        }
        return fills.putCount() - 1
    }

    fun addBorder() : Int {
        borders += node("border") {
            + node("left")
            + node("right")
            + node("top")
            + node("bottom")
            + node("diagonal")
        }
        return borders.putCount() -1
    }

    fun addCellStyleXf(numFmtId: Int) : Int = addXf(cellStyleXfs, numFmtId)
    fun addCellXf(numFmtId: Int) : Int = addXf(cellXfs, numFmtId)

    fun addCellStyle(name: String, xfId: String, builtinId: String) : Int {
        cellStyles += node("cellStyle") {
            this["name"] = name
            this["xfId"] = xfId
            this["builtinId"] = builtinId
        }
        return cellStyles.putCount() - 1
    }

    fun addCustomNumFmt(formatCode: String) : Int {
        val numFmtId = CUSTOM_NUM_FORMAT_ID_BASE + numFmts.size
        addNumFmt(numFmtId, formatCode)
        return addCellXf(numFmtId)
    }

    private fun addXf(xfs: Node, numFmtId: Int) : Int {
        xfs += node("xf") {
            this["numFmtId"] = numFmtId.toString()
        }
        return xfs.putCount() - 1
    }

    companion object {
        private const val CUSTOM_NUM_FORMAT_ID_BASE = 164
    }


}

