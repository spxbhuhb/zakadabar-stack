/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.backend

import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.stack.backend.server
import zakadabar.stack.backend.testing.TestCompanionBase
import zakadabar.stack.data.entity.EntityId
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

    @Test
    fun testBasic() = runBlocking {
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

}