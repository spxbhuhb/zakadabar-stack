/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import zakadabar.lib.xlsx.*
import zakadabar.lib.xlsx.dom.model.*
import zakadabar.lib.xlsx.dom.model.Cell
import zakadabar.lib.xlsx.dom.model.SharedStrings
import zakadabar.lib.xlsx.dom.model.WorkSheet
import zakadabar.lib.xlsx.model.XlsxCell
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.model.XlsxSheet

internal fun XlsxCell.toDom(sharedStrings : SharedStrings) : Cell? {

    if (isEmpty()) return null

    val coord = coordinate.coordinate
    val formatCode = format.ordinal

    return when(val v = value) {
        null -> null
        is String -> {
            val sharedStringId = sharedStrings.add(v)
            Cell(coord, sharedStringId, "s", formatCode)
        }
        is Boolean -> Cell(coord, if (v) "1" else "0", "b", formatCode)
        is Number -> Cell(coord, v, null, formatCode)
        is LocalDate -> Cell(coord, v.toInternal(), null, formatCode)
        is LocalDateTime -> Cell(coord, v.toInternal(), null, formatCode)
        is Instant -> Cell(coord, v.toInternal(), null, formatCode)
        else -> Cell(coord, v, "str", formatCode)
    }

}

internal fun XlsxSheet.toDom(sheetId: Int, sharedStrings : SharedStrings) : WorkSheet {
    val ws = WorkSheet(sheetId)

    var row : Row? = null

    cells.forEach {
        if (row?.rowNumber != it.coordinate.rowNumber) {
            row = Row(it.coordinate.rowNumber).apply(ws::addRow)
        }

        it.toDom(sharedStrings)?.let { cell->
            row!!.addCell(cell)
        }
    }

    return ws
}

internal fun XlsxFile.add(sheet: XlsxSheet) {
    val sheetId = workBook.nextSheetId()
    val ws = sheet.toDom(sheetId, sharedStrings)
    val workBookRelId = workBookRels.addRel(ws)
    workBook.addSheet(sheetId, workBookRelId, sheet.title)
    contentType.addPart(ws)
    content.add(ws)
}

internal fun XlsxDocument.toXlsxFile() = XlsxFile().apply { sheets.forEach(::add) }

internal val Part.content : ByteArray get() = when(this) {
    is SimpleDomElement -> toXml()
    else -> throw IllegalStateException("Content not supported: ${this::class}")
}

internal fun XlsxFile.toContentMap() : ContentMap {
    val fc = ContentMap()
    content.forEach {
        val path = it.partName.substringAfter('/')
        fc[path] = it::content
    }
    return fc
}

