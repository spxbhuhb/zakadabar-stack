/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.resource

import kotlin.test.Test
import kotlin.test.assertEquals

val globalVal by "global::value"

class LocalizedStringDelegateTest {

    val classVal by "class::value"

    companion object : LocalizationGroup(LocalizedStringDelegateTest::class) {
        val companionVal by "companion::value"
    }

    @Test
    fun testDelegates() {
        val localVal by "local::value"

        assertEquals("global::value", globalVal)
        assertEquals("class::value", classVal)
        assertEquals("companion::value", companionVal)
        assertEquals(localizedStrings["LocalizedStringDelegateTest.companionVal"], companionVal)
        assertEquals("local::value", localVal)

        val replaced =  "replaced global value"
        localizedStrings.map["globalVal"] = replaced
        assertEquals(replaced, globalVal)
    }

}