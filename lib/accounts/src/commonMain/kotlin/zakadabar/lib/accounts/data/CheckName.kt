/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.action.ActionBo
import zakadabar.stack.data.action.ActionBoCompanion
import zakadabar.stack.data.schema.BoSchema

/**
 * Checks if an account name is valid or is it already used.
 */
@Serializable
class CheckName(

    var accountName: String

) : ActionBo<CheckNameResult> {

    override suspend fun execute() = comm.action(this, serializer(), CheckNameResult.serializer())

    companion object : ActionBoCompanion(AccountPrivateBo.boNamespace)

    override fun schema() = BoSchema {
        + ::accountName min 1 max 50 blank false
    }

}