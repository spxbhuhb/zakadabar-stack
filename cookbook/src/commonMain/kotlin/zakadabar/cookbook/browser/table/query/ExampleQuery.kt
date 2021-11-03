/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.browser.table.query

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.cookbook.entity.builtin.ExampleBo
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

@Serializable
class ExampleQuery : QueryBo<List<ExampleBo>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(ExampleBo.serializer()))

    companion object : QueryBoCompanion(ExampleBo.boNamespace)

}