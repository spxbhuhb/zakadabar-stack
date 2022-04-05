/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene.business

import kotlinx.coroutines.*
import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.setting.setting
import zakadabar.core.util.Lock
import zakadabar.core.util.use
import zakadabar.lib.lucene.business.index.IndexFiles
import zakadabar.lib.lucene.business.search.SearchFiles
import zakadabar.lib.lucene.data.LuceneQuery
import zakadabar.lib.lucene.data.LuceneQueryResult
import zakadabar.lib.lucene.data.LuceneSettings
import zakadabar.lib.lucene.data.luceneBasic
import java.nio.file.Files
import java.nio.file.Paths

class LuceneBl : BusinessLogicCommon<BaseBo>() {

    override val namespace = luceneBasic

    val settings by setting<LuceneSettings>()

    override val router = router {
        query(LuceneQuery::class, ::luceneQuery)
    }

    override val authorizer by provider {
        this as SimpleRoleAuthorizer
        query(LuceneQuery::class, PUBLIC)
    }

    val indexingScope = CoroutineScope(Dispatchers.IO)

    var updateLock = Lock()

    var updateIndex: Boolean = false

    override

    fun onModuleStart() {
        indexingScope.launch { indexing() }
    }

    override fun onModuleStop() {
        indexingScope.cancel()
    }

    fun luceneQuery(executor: Executor, luceneQuery: LuceneQuery): List<LuceneQueryResult> =
        SearchFiles.search(luceneQuery, settings)

    // FIXME refine lucene index update triggers

    suspend fun indexing() {
        while (indexingScope.isActive) {
            updateLock.use {
                updateIndex = false
            }

            try {
                val params = if (Files.exists(Paths.get(settings.index))) {
                    arrayOf("-index", settings.index, "-docs", settings.docs, "-update")
                } else {
                    arrayOf("-index", settings.index, "-docs", settings.docs)
                }
                IndexFiles.main(params)
            } catch (ex: Exception) {
                logger.error("lucene index failed", ex)
            }

            while (! updateLock.use { updateIndex }) {
                delay(10000)
            }
        }
    }

}