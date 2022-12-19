/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

import zakadabar.lib.xlsx.*

fun XlsxCell.toDom(sharedStrings : SharedStrings) : Cell {
    //TODO: type handling!!
    val str = asString() ?: ""
    val strId = sharedStrings.add(str)
    val value = strId.toString()

    return Cell(coordinate.coordinate, value, "s")
}

fun XlsxRow.toDom(sharedStrings : SharedStrings) : Row {
    val r = Row(rowNumber)
    cells.asSequence().map{ it.toDom(sharedStrings) }.forEach(r::addCell)
    return r
}

fun XlsxSheet.toDom(sheetId: Int, sharedStrings : SharedStrings) : WorkSheet {
    val ws = WorkSheet(sheetId)
    rows.asSequence().map{ it.toDom(sharedStrings) }.forEach(ws::addRow)
    return ws
}

operator fun XlsxFile.plusAssign(sheet: XlsxSheet) {
    val sheetId = workBook.nextSheetId()
    val ws = sheet.toDom(sheetId, sharedStrings)
    val workBookRelId = workBookRels.addRel(ws)
    workBook.addSheet(sheetId, workBookRelId, sheet.title)
    contentType.addPart(ws)
    content.add(ws)
}

internal fun XlsxDocument.toXlsxFile() : XlsxFile {
    val xlsx = XlsxFile()
    sheets.forEach { xlsx += it }
    return xlsx
}

val Part.content : ByteArray get() = when(this) {
    is SimpleDomElement -> toXml()
    else -> throw IllegalStateException("Content not supported: ${this::class}")
}

internal fun XlsxFile.toContentMap() : ContentMap {
    val fc = ContentMap()
    content.forEach {
        val path = it.partName.substringAfter('/')
        fc[path] = { it.content }
    }
    return fc
}

fun XlsxDocument.toContentMap() : ContentMap = toXlsxFile().toContentMap()
