/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion
import zakadabar.core.schema.BoSchema


@Serializable
class LuceneQuery(

    var field: String = "content",
    var query: String = "",
    var hitsPerPage: Int = 10,
    var knnVectors : Int = 0

) : QueryBo<List<LuceneQueryResult>> {

    companion object : QueryBoCompanion(luceneBasic)

    override suspend fun execute(executor: Executor?, callConfig: CommConfig?): List<LuceneQueryResult> =
        comm.query(this, serializer(), ListSerializer(LuceneQueryResult.serializer()), executor, callConfig)

    override fun schema() = BoSchema {
        + ::field max 100
        + ::query blank false
        + ::hitsPerPage
        + ::knnVectors
    }

}