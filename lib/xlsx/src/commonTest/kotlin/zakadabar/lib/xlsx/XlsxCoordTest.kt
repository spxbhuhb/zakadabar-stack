/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.xlsx

import zakadabar.lib.xlsx.model.XlsxCoordinate
import kotlin.test.Test
import kotlin.test.assertEquals

class XlsxCoordTest {

    @Test
    fun testA1_1() {
        val c = XlsxCoordinate("A1")

        assertEquals("A", c.colLetter)
        assertEquals(1, c.colNumber)
        assertEquals(1, c.rowNumber)

    }

    @Test
    fun testA1_2() {
        val c = XlsxCoordinate(1,1)

        assertEquals("A", c.colLetter)
        assertEquals("A1", c.coordinate)

    }

    @Test
    fun testAA1_1() {
        val c = XlsxCoordinate("AA1")

        assertEquals("AA", c.colLetter)
        assertEquals(27, c.colNumber)
        assertEquals(1, c.rowNumber)

    }

    @Test
    fun testAA1_2() {
        val c = XlsxCoordinate(27,1)

        assertEquals("AA", c.colLetter)
        assertEquals("AA1", c.coordinate)

    }

}