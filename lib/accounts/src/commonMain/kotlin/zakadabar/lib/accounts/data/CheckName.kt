/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.data.schema.BoSchema

/**
 * Checks if an account name is valid or is it already used.
 *
 * TODO restrict checkName call number per session
 */
@Serializable
class CheckName(

    var accountName: String

) : QueryBo<CheckNameResult> {

    override suspend fun execute() = comm.query(this, serializer(), CheckNameResult.serializer())

    companion object : QueryBoCompanion(AccountPrivateBo.boNamespace)

    override fun schema() = BoSchema {
        + ::accountName min 1 max 50 blank false
    }

}

@Serializable
class CheckNameResult(

    var accountId: EntityId<AccountPublicBo>?

) : BaseBo