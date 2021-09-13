/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.setting

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.module.modules
import zakadabar.core.testing.TestCompanionBase
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
        assertEquals("hello2", s.nested.fromFile)

        val env = mapOf(
            "SETTING_TEST_FROMENVAUTO" to "world1",
            "FROM_ENV_EXP" to "world2",
            "SETTING_TEST_NESTED_FROMENVAUTO" to "world3",
            "NESTED_FROM_ENV_EXP" to "world4"
        )

        modules.first<SettingBl>().mergeEnvironmentAuto(s, "setting.test", env)
        modules.first<SettingBl>().mergeEnvironmentExplicit(s, env)

        assertEquals("world1", s.fromEnvAuto)
        assertEquals("world2", s.fromEnvExplicit)
        assertEquals("world3", s.nested.fromEnvAuto)
        assertEquals("world4", s.nested.fromEnvExplicit)

    }

}