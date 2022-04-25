/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.lucene.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.ActionBo
import zakadabar.core.data.ActionBoCompanion

/**
 * Start an update of the Lucene index.
 */
@Serializable
class UpdateIndex : ActionBo<Unit> {

    companion object : ActionBoCompanion(luceneBasic)

    override suspend fun execute(executor: Executor?, callConfig: CommConfig?) =
        comm.action(this, serializer(), Unit.serializer(), executor, callConfig)

}