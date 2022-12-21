/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx.dom.model

import zakadabar.lib.xlsx.dom.Part

internal class XlsxFile {

    val content = mutableListOf<Part>()

    val contentType = ContentType().also { content += it  }
    val rels = Rels().also { content += it  }
    val workBookRels = Rels("/xl/", "workbook.xml").also { content += it  }
    val workBook = WorkBook().also { content += it  }
    val sharedStrings = SharedStrings().also { content += it  }
    val styles = Styles().also { content += it  }

    init {

        contentType.addPart(workBook)
        contentType.addPart(styles)
        contentType.addPart(sharedStrings)

        rels.addRel(workBook)

        workBookRels.addRel(styles)
        workBookRels.addRel(sharedStrings)

    }

}