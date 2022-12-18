/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom

open class XlsxFile {

    val contentType = ContentType()
    val rels = Rels()
    val workBookRels = Rels("/xl/", "workbook.xml")
    val workBook = WorkBook()
    val sharedStrings = SharedStrings()
    val styles = Styles()

    val content = mutableListOf<Part>(contentType, rels, workBookRels, workBook, sharedStrings, styles)

    init {

        contentType.addPart(workBook)
        contentType.addPart(styles)
        contentType.addPart(sharedStrings)

        rels.addRel(workBook)

        workBookRels.addRel(styles)
        workBookRels.addRel(sharedStrings)

    }

}