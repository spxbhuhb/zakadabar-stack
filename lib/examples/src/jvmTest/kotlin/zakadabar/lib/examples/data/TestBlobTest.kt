/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.data

import io.ktor.client.request.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.authorize.UnsafeAuthorizer
import zakadabar.core.comm.CommBase
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.EntityId
import zakadabar.core.server.Server
import zakadabar.core.server.server
import zakadabar.lib.blobs.data.BlobCreateState
import zakadabar.lib.blobs.data.url
import zakadabar.lib.examples.backend.blob.TestBlobBl
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestBlobTest {

    companion object {

        @BeforeClass
        @JvmStatic
        fun setup() {
            server = Server("test")
            UnsafeAuthorizer.enabled = true
            server += TestBlobBl()
            server.main(arrayOf("--test"))
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            server.ktorServer.stop(1000, 10000)
        }
    }

    @Test
    fun testCreate() {
        runBlocking {
            CommConfig.global = CommConfig(baseUrl = "http://127.0.0.1:8888")

            val contentText = "almafa"
            val contentBytes = contentText.encodeToByteArray()

            val bo = TestBlob(EntityId(), null, "text", "test.txt", "text/plain", contentBytes.size.toLong()).create()

            val channel = Channel<Boolean>()

            bo.comm().upload(bo, contentBytes) { _, state, _ ->
                launch {
                    when (state) {
                        BlobCreateState.Error -> channel.send(false)
                        BlobCreateState.Done -> channel.send(true)
                        else -> Unit
                    }
                }
            }

            assertTrue(channel.receive())

            val readBack = CommBase.client.get<ByteArray>("${CommConfig.global.baseUrl}/${bo.url}")

            assertEquals(contentText, readBack.decodeToString())

        }
    }
}