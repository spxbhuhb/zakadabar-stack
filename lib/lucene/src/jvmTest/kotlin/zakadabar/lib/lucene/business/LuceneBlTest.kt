/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene.business

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import zakadabar.core.module.modules
import zakadabar.core.testing.TestCompanionBase
import zakadabar.core.util.use
import zakadabar.lib.lucene.data.LuceneQuery
import zakadabar.lib.lucene.data.UpdateIndex
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LuceneBlTest {

    companion object : TestCompanionBase() {

        override fun addModules() {
            modules += LuceneBl()
        }

        @BeforeClass
        @JvmStatic
        override fun setup() = super.setup()

        @AfterClass
        @JvmStatic
        override fun teardown() = super.teardown()

    }

    @Test
    fun `index and search`() = runBlocking {
        val bl = modules.first<LuceneBl>()

        bl.updateIndex(mockSo, UpdateIndex())

        while (bl.indexingLock.use { bl.indexing }) {
            delay(100)
        }

        assertFailsWith<IllegalArgumentException> {
            bl.luceneQuery(mockAnonymous, LuceneQuery(query = ""))
        }

        val c2 = bl.luceneQuery(mockAnonymous, LuceneQuery(query = "content")).sortedBy { it.path }
        assertEquals(2, c2.size)
        assertEquals("doc1.md", c2[0].path)
        assertEquals("doc2.md", c2[1].path)
        assertEquals("lucene test document 1", c2[0].title)
        assertEquals("lucene test document 2", c2[1].title)

        val c3 = bl.luceneQuery(mockAnonymous, LuceneQuery(query = "content-1")).sortedBy { it.path }
        assertEquals(2, c3.size)
        assertEquals("doc1.md", c3[0].path)
        assertEquals("lucene test document 1", c3[0].title)

    }
}