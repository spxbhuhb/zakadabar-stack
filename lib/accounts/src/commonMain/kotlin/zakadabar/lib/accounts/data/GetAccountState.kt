/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.query.QueryBo
import zakadabar.core.data.query.QueryBoCompanion

/**
 * Get the state of one account. Intended for management, requires
 * security officer role.
 */
@Serializable
class GetAccountState(

    var accountId: EntityId<AccountPrivateBo>

) : QueryBo<AccountStateBo> {

    override suspend fun execute() = comm.query(this, serializer(), AccountStateBo.serializer())

    companion object : QueryBoCompanion(AccountPrivateBo.boNamespace)

}