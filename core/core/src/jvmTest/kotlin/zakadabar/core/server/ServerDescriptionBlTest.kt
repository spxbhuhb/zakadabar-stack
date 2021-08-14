/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.testing.TestCompanionBase
import zakadabar.core.data.builtin.misc.ServerDescriptionQuery
import kotlin.test.assertEquals

class ServerDescriptionBlTest {

    companion object : TestCompanionBase() {

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun `server description query`() = runBlocking {

        val bo = ServerDescriptionQuery().execute()

        assertEquals(server.description.name, bo.name)
        assertEquals(server.description.version, bo.version)
        assertEquals(server.description.defaultLocale, bo.defaultLocale)

    }

}
