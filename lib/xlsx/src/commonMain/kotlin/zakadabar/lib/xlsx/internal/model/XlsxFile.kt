/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.internal.model

internal class XlsxFile {


    val content = mutableListOf<Part>()

    val contentType = ContentType().also(content::add)
    val rels = Rels().also(content::add)
    val workBookRels = Rels("/xl/", "workbook.xml").also(content::add)
    val workBook = WorkBook().also(content::add)
    val sharedStrings = SharedStrings().also(content::add)
    val styles = Styles().also(content::add)

    init {

        contentType.addPart(workBook)
        contentType.addPart(styles)
        contentType.addPart(sharedStrings)

        rels.addRel(workBook)

        workBookRels.addRel(styles)
        workBookRels.addRel(sharedStrings)

    }

    fun addWorkSeet(sheetId: Int, title: String, ws: WorkSheet) {
        val workBookRelId = workBookRels.addRel(ws)
        workBook.addSheet(sheetId, workBookRelId, title)
        contentType.addPart(ws)
        content.add(ws)
    }


}