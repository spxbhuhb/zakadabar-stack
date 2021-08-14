/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.builtin.account.AccountPublicBo
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.query.QueryBo
import zakadabar.core.data.query.QueryBoCompanion
import zakadabar.core.util.PublicApi

/**
 * Query accounts. Requires the user to be logged in.
 */
@Serializable
class AccountList : QueryBo<List<AccountPublicBo>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(AccountPublicBo.serializer()))

    companion object : QueryBoCompanion(AccountPrivateBo.boNamespace)

}