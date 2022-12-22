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

    val coord = coordinate.coordinate
    val formatCode = format.ordinal
    val timeZone = sheet.doc.timeZone

    return when(val v = value) {
        null -> null
        is String -> {
            val sharedStringId = sharedStrings.addString(v)
            Cell(coord, sharedStringId, "s", formatCode)
        }
        is Boolean -> Cell(coord, if (v) "1" else "0", "b", formatCode)
        is Number -> Cell(coord, v, null, formatCode)
        is LocalDate -> Cell(coord, v.toInternal(), null, formatCode)
        is LocalDateTime -> Cell(coord, v.toInternal(), null, formatCode)
        is Instant -> Cell(coord, v.toInternal(timeZone), null, formatCode)
        else -> Cell(coord, v, "str", formatCode)
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
