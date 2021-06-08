/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

@Serializable
class OverviewQuery: QueryBo<Overview> {

    companion object : QueryBoCompanion(ContentCommonBo.boNamespace)

    override suspend fun execute() = comm.query(this, serializer(), Overview.serializer())

}