/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.server.ktor

import io.ktor.auth.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.server.server
import zakadabar.core.testing.TestCompanionBase

class KtorConfigBuilderTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            server += KtorAuthConfig {
                basic {

                }
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
    fun testConfigBuilder() {
        // cannot test this because fields are private / internal... meh...
    }
}