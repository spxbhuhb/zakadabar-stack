/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.action.nullresult

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.stack.backend.server
import zakadabar.stack.backend.testing.TestCompanionBase
import kotlin.test.assertEquals
import kotlin.test.assertNull

class NullActionTest {

    companion object : TestCompanionBase() {

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

        override fun addModules() {
            server += ActionBl()
        }
    }

    @Test
    fun testNullAction() = runBlocking {
        assertNull(Action(null).execute())
        assertEquals("hello", Action("hello").execute()?.value)
    }

}