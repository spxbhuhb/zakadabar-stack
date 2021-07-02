/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.misc

import kotlinx.serialization.Serializable
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

@Serializable
open class ServerDescriptionQuery : QueryBo<ServerDescriptionBo> {

    override suspend fun execute() = comm.query(this, serializer(), ServerDescriptionBo.serializer())

    companion object : QueryBoCompanion("server-description")

}
