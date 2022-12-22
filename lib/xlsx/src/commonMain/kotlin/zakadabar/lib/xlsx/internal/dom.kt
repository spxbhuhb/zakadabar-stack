/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import zakadabar.lib.xlsx.*
import zakadabar.lib.xlsx.dom.Node
import zakadabar.lib.xlsx.dom.toXml
import zakadabar.lib.xlsx.internal.model.Cell
import zakadabar.lib.xlsx.internal.model.Part
import zakadabar.lib.xlsx.internal.model.Row
import zakadabar.lib.xlsx.internal.model.SharedStrings
import zakadabar.lib.xlsx.internal.model.WorkSheet
import zakadabar.lib.xlsx.internal.model.XlsxFile
import zakadabar.lib.xlsx.model.XlsxCell
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.model.XlsxSheet

internal fun XlsxCell.toDom(sharedStrings : SharedStrings) : Cell? {

    val conf = sheet.doc.conf
    val strings = conf.strings
    val timeZone = conf.timeZone
    val coord = coordinate.coordinate

    return when(val v = value) {
        null -> null
        is String -> {
            val sharedStringId = sharedStrings.addString(v)
            Cell(coord, sharedStringId, Cell.Type.SHARED_STRING, numberFormat)
        }
        is Enum<*> -> {
            val str = if (conf.localizedEnums) strings.getNormalized(v.name) else v.name
            val sharedStringId = sharedStrings.addString(str)
            Cell(coord, sharedStringId, Cell.Type.SHARED_STRING, numberFormat)
        }
        is Boolean -> {
            if (conf.localizedBooleans) {
                val str = if (v) strings.trueText else strings.falseText
                val sharedStringId = sharedStrings.addString(str)
                Cell(coord, sharedStringId, Cell.Type.SHARED_STRING, numberFormat)
            } else {
                Cell(coord, if (v) "1" else "0", Cell.Type.BOOLEAN, numberFormat)
            }
        }
        is Number -> Cell(coord, v, Cell.Type.NORMAL, numberFormat)
        is LocalDate -> Cell(coord, v.toInternal(), Cell.Type.NORMAL, numberFormat)
        is LocalDateTime -> Cell(coord, v.toInternal(), Cell.Type.NORMAL, numberFormat)
        is Instant -> Cell(coord, v.toInternal(timeZone), Cell.Type.NORMAL, numberFormat)
        else -> Cell(coord, v, Cell.Type.STRING, numberFormat)
    }
}

internal fun XlsxSheet.toDom(sheetId: Int, sharedStrings : SharedStrings) : WorkSheet {
    val ws = WorkSheet(sheetId)

    var row : Row? = null

    cells.forEach { xc->
        val cell = xc.toDom(sharedStrings) ?: return@forEach
        val rowNumber = xc.coordinate.rowNumber
        if (row?.rowNumber != rowNumber) {
            row = Row(rowNumber).apply(ws::addRow)
        }
        row?.addCell(cell)
    }

    return ws
}

internal fun XlsxDocument.toXlsxFile() : XlsxFile {
    val f = XlsxFile()
    sheets.forEach { xs->
        val sheetId = f.workBook.nextSheetId()
        val ws = xs.toDom(sheetId, f.sharedStrings)
        f.addWorkSeet(sheetId, xs.title, ws)
    }
    return f
}

internal val Part.content : ByteArray get() = when(this) {
    is Node -> toXml()
    else -> throw IllegalStateException("Content not supported: ${this::class}")
}

internal fun XlsxFile.toContentMap() : ContentMap {
    val cm = ContentMap()
    content.forEach {
        val path = it.partName.substringAfter('/')
        cm[path] = it::content
    }
    return cm
}

internal fun Node.putCount() : Int {
    val count = elements.size
    attributes["count"] = count.toString()
    return count
}
