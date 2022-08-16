/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene.business

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import zakadabar.core.authorize.Executor
import zakadabar.core.authorize.SimpleRoleAuthorizer
import zakadabar.core.authorize.appRoles
import zakadabar.core.business.BusinessLogicCommon
import zakadabar.core.data.BaseBo
import zakadabar.core.setting.setting
import zakadabar.core.util.Lock
import zakadabar.core.util.use
import zakadabar.lib.lucene.business.index.IndexFiles
import zakadabar.lib.lucene.business.search.SearchFiles
import zakadabar.lib.lucene.data.*
import java.nio.file.Files
import java.nio.file.Paths

class LuceneBl(
    val mapResult : (executor : Executor, result : LuceneQueryResult) -> LuceneQueryResult? = { _,r -> r }
) : BusinessLogicCommon<BaseBo>() {

    override val namespace = luceneBasic

    val settings by setting<LuceneSettings>()

    override val router = router {
        query(LuceneQuery::class, ::luceneQuery)
        action(UpdateIndex::class, ::updateIndex)
    }

    override val authorizer by provider {
        this as SimpleRoleAuthorizer
        query(LuceneQuery::class, PUBLIC)
        action(UpdateIndex::class, appRoles.securityOfficer)
    }

    val indexingScope = CoroutineScope(Dispatchers.IO)

    var indexingLock = Lock()

    var indexingNeeded: Boolean = false

    var indexing: Boolean = false

    /**
     * Executes a query.
     */
    @Suppress("UNUSED_PARAMETER")
    fun luceneQuery(executor: Executor, luceneQuery: LuceneQuery): List<LuceneQueryResult> =
        SearchFiles
            .search(luceneQuery, settings)
            .mapNotNull { mapResult(executor, it) }

    /**
     * Launches [index] when it is not running. If it is running, sets [indexingNeeded]
     * to true and returns. Just before returning [index] checks [indexingNeeded] and
     * re-runs itself if when true. This ensures that not two indexing operations run
     * at the same time and no index request is lost.
     */
    @Suppress("UNUSED_PARAMETER")
    fun updateIndex(executor: Executor, action: UpdateIndex) {
        indexingLock.use {
            if (indexing) {
                indexingNeeded = true
            } else {
                indexing = true
                indexingNeeded = false
                indexingScope.launch { index() }
            }
        }
    }

    /**
     * Performs indexing of the documents. When the index directory exists
     * it updates the index, otherwise the index is rebuilt.
     */
    fun index() {
        do {
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

            val rerun = indexingLock.use {
                if (indexingNeeded) {
                    indexingNeeded = false
                    true
                } else {
                    indexing = false
                    false
                }
            }

        } while(rerun)
    }
}