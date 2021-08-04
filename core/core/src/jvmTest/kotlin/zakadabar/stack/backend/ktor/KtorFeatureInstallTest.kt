/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.ktor

import io.ktor.application.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.stack.backend.server
import zakadabar.stack.backend.testing.TestCompanionBase
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class KtorFeatureInstallTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            server += TestFeature

            server += TestFeature2 {
                value = "world"
            }
        }

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun testFeatureInstall() {
        val tf = server.ktorServer.application.featureOrNull(TestFeature)
        assertNotNull(tf)

        val tf2 = server.ktorServer.application.featureOrNull(TestFeature2)
        assertNotNull(tf2)
        assertEquals("world", tf2.value)

    }
}