/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.testing.TestCompanionBase
import zakadabar.core.module.modules
import java.nio.file.Paths
import kotlin.test.assertEquals


class SettingBlTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            modules += SettingBl(settingsDirectory = Paths.get("./template/test"))
        }

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun `environment merge`() = runBlocking {
        val s by setting<SettingTestBo>("setting.test")

        assertEquals("hello", s.fromFile)

        val env = mapOf("SETTING_TEST_FROMENV" to "world")
        modules.first<SettingBl>().mergeEnvironment(s, "setting.test", env)

        assertEquals("world", s.fromEnv)
    }

}