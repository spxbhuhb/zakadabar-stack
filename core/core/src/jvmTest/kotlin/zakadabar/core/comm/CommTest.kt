/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig.Companion.commConfig
import zakadabar.core.data.EntityId
import zakadabar.core.data.create
import zakadabar.core.server.server
import zakadabar.core.testing.TestCompanionBase
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CommTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            server += TestBl()
        }

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun `default config test`() = runBlocking {

        commConfig = CommConfig(baseUrl)

        val created = TestBo.create {
            name = "test-name"
        }

        TestBo.all().also {
            assertEquals(1, it.size)
            assertEquals("test-name", it[0].name)
        }

        TestBo.create {
            name = "test-name-2"
        }

        assertTrue(created.id.isNotEmpty())

        TestBo.all().also {
            assertEquals(2, it.size)
            assertEquals(1, it.filter { entry -> entry.name == "test-name"}.size)
            assertEquals(1, it.filter { entry -> entry.name == "test-name-2"}.size)
            assertTrue(it[0].id != it[1].id)
        }

        val readBack = TestBo.read(created.id)

        assertEquals(created.id, readBack.id)
        assertEquals(created.name, readBack.name)

        created.update {
            name = "test-update"
        }

        val readBackUpdate = TestBo.read(created.id)

        assertEquals(created.id, readBackUpdate.id)
        assertEquals("test-update", readBackUpdate.name)

        created.delete()

        TestBo.all().also {
            assertEquals(1, it.size)
            assertEquals("test-name-2", it[0].name)
        }

        Unit
    }

    @Test
    fun `local BL call test`() = runBlocking {
        val executor = Executor(EntityId("0"),true, emptyList(), emptyList())

        commConfig = CommConfig(local = true)

        val created = TestBo.create(executor) {
            name = "test-name"
        }

        TestBo.all(executor).also {
            assertEquals(1, it.size)
            assertEquals("test-name", it[0].name)
        }

        TestBo.create(executor) {
            name = "test-name-2"
        }

        assertTrue(created.id.isNotEmpty())

        TestBo.all(executor).also {
            assertEquals(2, it.size)
            assertEquals(1, it.filter { entry -> entry.name == "test-name"}.size)
            assertEquals(1, it.filter { entry -> entry.name == "test-name-2"}.size)
            assertTrue(it[0].id != it[1].id)
        }

        val readBack = TestBo.read(created.id, executor)

        assertEquals(created.id, readBack.id)
        assertEquals(created.name, readBack.name)

        created.update(executor) {
            name = "test-update"
        }

        val readBackUpdate = TestBo.read(created.id, executor)

        assertEquals(created.id, readBackUpdate.id)
        assertEquals("test-update", readBackUpdate.name)

        created.delete(executor)

        TestBo.all(executor).also {
            assertEquals(1, it.size)
            assertEquals("test-name-2", it[0].name)
        }

        Unit
    }
}
