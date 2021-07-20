/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.query.nullresult

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.stack.backend.server
import zakadabar.stack.backend.testing.TestCompanionBase
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NullQueryTest {

    companion object : TestCompanionBase() {

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

        override fun addModules() {
            server += QueryBl()
        }
    }

    @Test
    fun testNullQuery() = runBlocking {
        assertNull(Query(null).execute())
        assertEquals("hello", Query("hello").execute()?.value)
    }

}