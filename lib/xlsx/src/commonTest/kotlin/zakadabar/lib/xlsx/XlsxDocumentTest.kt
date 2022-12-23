/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import kotlinx.datetime.*
import setRow
import toContentMap
import zakadabar.lib.xlsx.model.XlsxDocument
import zakadabar.lib.xlsx.model.XlsxSheet
import kotlin.test.Test

class XlsxDocumentTest {

    @Test
    fun testWriteFile() {

        println("Hello Test")

        val xlsx = XlsxDocument()

        val sheet1 = XlsxSheet("Stuff Members")
        xlsx += sheet1

        sheet1.setRow("A1", listOf("name", "birth", "height", "dead"))

        (2..10).forEach { row->
            sheet1[1, row].value = "Laci"
            sheet1[2, row].value = Clock.System.now()
            sheet1[3, row].value = row / 2.0
            sheet1[4, row].value = row % 2 == 0
        }

        val sheet2 = XlsxSheet("Summary")
        xlsx += sheet2

        sheet2["A1"].value = "summary"
        sheet2["B1"].value = "none"

        val fc = xlsx.toContentMap()
        fc.saveXlsx("build/test.xlsx")

    }


}