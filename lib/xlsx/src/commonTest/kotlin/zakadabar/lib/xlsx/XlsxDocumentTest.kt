/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import kotlinx.datetime.*
import fillRow
import save
import zakadabar.lib.xlsx.model.XlsxDocument
import kotlin.test.Test

class XlsxDocumentTest {

    @Test
    fun testWriteFile() {

        val doc = XlsxDocument()
        val sheet = doc.newSheet("T2 Database C&A")

        sheet.columns["A"].width = 15.0
        sheet.columns["B"].width = 11.4

        sheet["A1"].value = "Name"
        sheet["B1"].value = "Date of birth"
        sheet["C1"].value = "Still alive"

        sheet.fillRow("A2", listOf("John Connor", LocalDate(1985, 2, 28), true))
        sheet.fillRow("A3", listOf("Sarah Connor", LocalDate(1964, 8, 13), true))

        doc.save("build/terminator.xlsx")

    }


}