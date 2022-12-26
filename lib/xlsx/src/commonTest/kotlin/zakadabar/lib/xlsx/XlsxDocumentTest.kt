/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import kotlinx.datetime.*
import fillRow
import save
import zakadabar.lib.xlsx.conf.XlsxConfiguration
import zakadabar.lib.xlsx.model.XlsxDocument
import kotlin.random.Random
import kotlin.test.Test

class XlsxDocumentTest {

    @Test
    fun testWriteFile() {

        val cfg = XlsxConfiguration()
        val roundedAndThousandSeparatedNumberFormat = cfg.formats.CustomNumberFormat("#,##0.000")

        val doc = XlsxDocument(cfg)

        val sheet = doc.newSheet("T2 Database & Task")

        sheet.columns["A"].width = 18.5
        sheet.columns["B"].width = 12.5

        sheet["A1"].value = "Name"
        sheet["B1"].value = "Date of birth"
        sheet["C1"].value = "Still alive"

        sheet.fillRow("A2", listOf("John Connor", LocalDate(1985, 2, 28), true))
        sheet.fillRow("A3", listOf("Sarah Connor", LocalDate(1964, 8, 13), true))


        for(i in 8..100_000) {
            sheet[1,i].value = "Alma-$i"
            sheet[2,i].value = i % 2 == 0
            sheet[3,i].value = Clock.System.now()
        }



        val summary = doc.newSheet("Summary")

        summary.columns["A"].width = 18.2
        summary.columns["B"].width = 22.5

        summary["A1"].value = "Mission start"
        summary["B1"].value = Clock.System.now()

        summary["A2"].value = "Population to examine"
        summary["B2"].value = Random.nextDouble(9_000_000_000.0, 20_000_000_000.0)
        summary["B2"].numberFormat = roundedAndThousandSeparatedNumberFormat


        doc.save("build/terminator.xlsx")

    }


}