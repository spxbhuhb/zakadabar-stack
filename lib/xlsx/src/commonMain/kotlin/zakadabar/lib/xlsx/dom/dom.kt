/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

import zakadabar.lib.xlsx.*

fun XlsxCell.toDom(sharedStrings : SharedStrings) : SimpleDomElement {
    val c = SimpleDomElement.of("c", "r" to coordinate.coordinate, "t" to "s")
    //type handling!!

    val str = asString() ?: ""
    val strId = sharedStrings.add(str)

    c.childNodes += SimpleDomElement("v",  strId.toString())
    return c
}

fun XlsxRow.toDom(sharedStrings : SharedStrings) : SimpleDomElement {
    val r = SimpleDomElement.of("row", "r" to rowNumber.toString())
    cells.forEach { r += it.toDom(sharedStrings) }
    return r
}

fun XlsxSheet.toDom(sheetId: Int, sharedStrings : SharedStrings) : WorkSheet {
    val ws = WorkSheet(sheetId)
    rows.forEach { ws.sheetData += it.toDom(sharedStrings) }
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

internal fun XlsxFile.toFileContainer() : FileContainer {
    val fc = FileContainer()

    content.forEach { part->
        (part as? SimpleDomElement)?.toXml()?.let {
            fc.add(part.partName.substringAfter('/'), it.encodeToByteArray())
        }
    }

    return fc
}

fun XlsxDocument.toFileContainer() : FileContainer = toXlsxFile().toFileContainer()
