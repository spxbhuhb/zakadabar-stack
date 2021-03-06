/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.backend.authorize

import kotlinx.serialization.Serializable
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

@Serializable
class TestQuery : QueryBo<TestQuery> {
    override suspend fun execute(): TestQuery {
        throw NotImplementedError("this is just a test query")
    }

    companion object : QueryBoCompanion("not-used")
}