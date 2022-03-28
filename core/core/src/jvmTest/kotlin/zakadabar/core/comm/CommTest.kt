/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.comm

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig.Companion.global
import zakadabar.core.data.EntityId
import zakadabar.core.data.create
import zakadabar.core.server.server
import zakadabar.core.testing.TestCompanionBase
import kotlin.test.assertEquals
import kotlin.test.assertNull
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

        transaction {
            TestTable.deleteAll()
        }

        global = CommConfig(baseUrl)

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

        assertEquals("test-action", TestAction("test-action").execute().value)
        assertEquals("test-action-null", TestActionNull("test-action-null").execute()?.value)
        assertNull(TestActionNull(null).execute())
        assertEquals("test-query", TestQuery("test-query").execute().value)
        assertEquals("test-query-null", TestQueryNull("test-query-null").execute()?.value)
        assertNull(TestQueryNull(null).execute())
    }

    @Test
    fun `local BL call test - global config`() = runBlocking {
        val executor = Executor(EntityId("0"),true, emptyList(), emptyList())

        transaction {
            TestTable.deleteAll()
        }

        global = CommConfig(local = true)

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

        assertEquals("test-action", TestAction("test-action").execute(executor).value)
        assertEquals("test-action-null", TestActionNull("test-action-null").execute(executor)?.value)
        assertNull(TestActionNull(null).execute(executor))
        assertEquals("test-query", TestQuery("test-query").execute(executor).value)
        assertEquals("test-query-null", TestQueryNull("test-query-null").execute(executor)?.value)
        assertNull(TestQueryNull(null).execute(executor))
    }

    @Test
    fun `local BL call test - call config`() = runBlocking {
        val executor = Executor(EntityId("0"),true, emptyList(), emptyList())

        transaction {
            TestTable.deleteAll()
        }

        // set the global config to default, this fails without the call config
        // because there is no base URL set (that is intentional)

        global = CommConfig()
        val callConfig = CommConfig(local = true)

        val created = TestBo.create(executor, callConfig) {
            name = "test-name"
        }

        TestBo.all(executor, callConfig).also {
            assertEquals(1, it.size)
            assertEquals("test-name", it[0].name)
        }

        TestBo.create(executor, callConfig) {
            name = "test-name-2"
        }

        assertTrue(created.id.isNotEmpty())

        TestBo.all(executor, callConfig).also {
            assertEquals(2, it.size)
            assertEquals(1, it.filter { entry -> entry.name == "test-name"}.size)
            assertEquals(1, it.filter { entry -> entry.name == "test-name-2"}.size)
            assertTrue(it[0].id != it[1].id)
        }

        val readBack = TestBo.read(created.id, executor, callConfig)

        assertEquals(created.id, readBack.id)
        assertEquals(created.name, readBack.name)

        created.update(executor, callConfig) {
            name = "test-update"
        }

        val readBackUpdate = TestBo.read(created.id, executor, callConfig)

        assertEquals(created.id, readBackUpdate.id)
        assertEquals("test-update", readBackUpdate.name)

        created.delete(executor, callConfig)

        TestBo.all(executor, callConfig).also {
            assertEquals(1, it.size)
            assertEquals("test-name-2", it[0].name)
        }

        assertEquals("test-action", TestAction("test-action").execute(executor, callConfig).value)
        assertEquals("test-action-null", TestActionNull("test-action-null").execute(executor, callConfig)?.value)
        assertNull(TestActionNull(null).execute(executor, callConfig))
        assertEquals("test-query", TestQuery("test-query").execute(executor, callConfig).value)
        assertEquals("test-query-null", TestQueryNull("test-query-null").execute(executor, callConfig)?.value)
        assertNull(TestQueryNull(null).execute(executor, callConfig))
    }
}
