/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene

import zakadabar.core.authorize.Executor
import zakadabar.core.module.modules
import zakadabar.lib.lucene.business.LuceneBl
import zakadabar.lib.lucene.data.LuceneQueryResult

fun install(
    mapResult: (executor: Executor, result: LuceneQueryResult) -> LuceneQueryResult? = { _, r -> r }
) {
    modules += LuceneBl(mapResult)
}