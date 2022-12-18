/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import zakadabar.lib.xlsx.dom.toFileContainer
import kotlin.test.Test

class XlsxDocumentTest {

    @Test
    fun testWriteFile() {

        println("Hello Test")

        val xlsx = XlsxDocument()

        val sheet1 = XlsxSheet("Stuff Members")

        sheet1.addCell("A1", "name")
        sheet1.addCell("B1", "birth")
        sheet1.addCell("C1", "height")

        (2..10_000).forEach {row->
            sheet1.addCell(XlsxCoordinate(row, 1), "Laci")
            sheet1.addCell(XlsxCoordinate(row, 2), "1971.07.27")
            sheet1.addCell(XlsxCoordinate(row, 3), "${row / 2.0}")
        }

        xlsx += sheet1

        val sheet2 = XlsxSheet("Summary")
        sheet2.addCell("A1", "summary")
        sheet2.addCell("B1", "none")

        xlsx += sheet2

        val fc = xlsx.toFileContainer()
        fc.saveZip("build/test.xlsx")
    }
}