/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

class Styles : SimpleDomElement("styleSheet"), Part {

    override val partName = "/xl/styles.xml"
    override val contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml"
    override val relType = "http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles"

    val numFmts = of("numFmts")
    val fonts = of("fonts")
    val fills = of("fills")
    val borders = of("borders")
    val cellStyleXfs = of("cellStyleXfs")
    val cellXfs = of("cellXfs")
    val cellStyles = of("cellStyles")

    init {
        attributes["xmlns"] = "http://schemas.openxmlformats.org/spreadsheetml/2006/main"

        childNodes += numFmts
        childNodes += fonts
        childNodes += fills
        childNodes += borders
        childNodes += cellStyleXfs
        childNodes += cellXfs
        childNodes += cellStyles

        addNumFmts()
        addFont("11", "1", "Calibri", "2", "minor")
        addPatternFill("none")
        addBorder()
        addCellStyleXfs()
        addCellXfs()
        addCellStyles()

    }

    private fun adjustCount(e: SimpleDomElement) {
        e.attributes["count"] = e.childNodes.size.toString()
    }

    fun addFont(sz: String, colorTheme: String, name: String, family: String, scheme: String) : Int {
        val index = fonts.childNodes.size
        fonts += of("font").also {
            it += of("sz", "val" to sz)
            it += of("color", "theme" to colorTheme)
            it += of("name", "val" to name)
            it += of("family", "val" to family)
            it += of("scheme", "val" to scheme)
        }
        adjustCount(fonts)
        return index
    }

    fun addPatternFill(patternType: String) : Int {
        val index = fills.childNodes.size
        fills += of("fill").also {
            it += of("patternFill", "patternType" to patternType)
        }
        adjustCount(fills)
        return index
    }

    fun addBorder() : Int {
        val index = borders.childNodes.size
        borders += of("border").also {
            it += of("left")
            it += of("right")
            it += of("top")
            it += of("bottom")
            it += of("diagonal")
        }
        adjustCount(borders)
        return index
    }

    fun addCellStyleXfs() : Int {
        val index = cellStyleXfs.childNodes.size
        cellStyleXfs += of("xf", "numFmtId" to "0")
        adjustCount(cellStyleXfs)
        return index
    }

    fun addCellXfs() : Int {
        val index = cellXfs.childNodes.size
        cellXfs += of("xf", "numFmtId" to "0", "xfId" to "0")
        cellXfs += of("xf", "numFmtId" to "14", "xfId" to "0", "applyNumberFormat" to "1")
        cellXfs += of("xf", "numFmtId" to "164", "applyNumberFormat" to "1")
        adjustCount(cellXfs)
        return index
    }

    fun addCellStyles() : Int {
        val index = cellStyles.childNodes.size
        cellStyles += of("cellStyle", "name" to "normal", "xfId" to "0", "builtinId" to "0")
        adjustCount(cellStyles)
        return index
    }

    fun addNumFmts() : Int {
        val index = numFmts.childNodes.size
        numFmts += of("numFmt", "numFmtId" to "164", "formatCode" to "h:mm")
        adjustCount(numFmts)
        return index
    }

}

