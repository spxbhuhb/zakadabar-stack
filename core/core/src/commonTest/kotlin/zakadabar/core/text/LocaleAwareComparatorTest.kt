/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.text

import kotlin.test.Test
import kotlin.test.assertEquals

class LocaleAwareComparatorTest {

    @Test
    fun sortingWithLocale(){

        val unsorted = listOf("alma","álom","ÁLOM","ALMA","b","c","X", "alom", "ALOM", "ál").shuffled()

        val actual = unsorted.sortedWithLocale()
        val expected = listOf("ál", "alma", "ALMA", "alom", "ALOM", "álom", "ÁLOM", "b", "c", "X")

        assertEquals(expected, actual)
    }

}