/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.business

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.EntityId
import zakadabar.core.server.server
import zakadabar.core.testing.TestCompanionBase
import zakadabar.core.util.UUID
import kotlin.test.assertEquals

class BlobBlTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            server += TestBlobBl()
        }

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    fun clearDb() {
        transaction {
            TestBlobExposedTable.deleteAll()
            TestBlobReferenceExposedTable.deleteAll()
        }
    }

    @Test
    fun `basic operations test`() = runBlocking {

        clearDb()

        CommConfig.global = CommConfig(baseUrl)

        val contentText = "almafa"
        val contentBytes = contentText.encodeToByteArray()

        val bo = TestBlob(EntityId(), "", null, "test.txt", "text/plain", 0)
            .create()
            .upload(contentBytes)

        assertEquals(contentText, bo.download().decodeToString())
        assertEquals(contentText, TestBlob.download(bo.id).decodeToString())

        val content2Text = "almafa2"
        val content2Bytes = content2Text.encodeToByteArray()

        bo.upload(content2Bytes)

        assertEquals(content2Text, bo.download().decodeToString())

        val list = TestBlob.byReference(null)
        assertEquals(1, list.size)
        assertEquals(bo.id, list[0].id)
        assertEquals(content2Bytes.size, bo.size.toInt())
        assertEquals(content2Bytes.size, TestBlob.read(bo.id).size.toInt())
    }

    @Test
    fun `basic operations test - local as default`() = runBlocking {

        clearDb()

        val executor = Executor(EntityId("0"), UUID.NIL, true, emptySet(), emptySet(), emptySet(), emptySet())
        CommConfig.global = CommConfig(local = true)

        val contentText = "almafa"
        val contentBytes = contentText.encodeToByteArray()

        val bo = TestBlob(EntityId(), "", null, "test.txt", "text/plain", 0)
            .create(executor)
            .upload(contentBytes, executor)

        assertEquals(contentText, bo.download(executor).decodeToString())
        assertEquals(contentText, TestBlob.download(bo.id, executor).decodeToString())

        val content2Text = "almafa2"
        val content2Bytes = content2Text.encodeToByteArray()

        bo.upload(content2Bytes, executor)

        assertEquals(content2Text, bo.download(executor).decodeToString())

        val list = TestBlob.byReference(null, executor = executor)
        assertEquals(1, list.size)
        assertEquals(bo.id, list[0].id)
        assertEquals(content2Bytes.size, bo.size.toInt())
        assertEquals(content2Bytes.size, TestBlob.read(bo.id, executor).size.toInt())
    }

    @Test
    fun `basic operations test - local per call`() = runBlocking {

        clearDb()

        val executor = Executor(EntityId("0"), UUID.NIL, true, emptySet(), emptySet(), emptySet(), emptySet())
        val config = CommConfig(local = true)

        val contentText = "almafa"
        val contentBytes = contentText.encodeToByteArray()

        val bo = TestBlob(EntityId(), "", null, "test.txt", "text/plain", 0)
            .create(executor, config)
            .upload(contentBytes, executor, config)

        assertEquals(contentText, bo.download(executor, config).decodeToString())
        assertEquals(contentText, TestBlob.download(bo.id, executor, config).decodeToString())

        val content2Text = "almafa2"
        val content2Bytes = content2Text.encodeToByteArray()

        bo.upload(content2Bytes, executor, config)

        assertEquals(content2Text, bo.download(executor, config).decodeToString())

        val list = TestBlob.byReference(null, null, executor, config)
        assertEquals(1, list.size)
        assertEquals(bo.id, list[0].id)
        assertEquals(content2Bytes.size, bo.size.toInt())
        assertEquals(content2Bytes.size, TestBlob.read(bo.id, executor).size.toInt())
    }
}