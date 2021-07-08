/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.SelectOptionProvider
import zakadabar.stack.data.builtin.account.AccountPublicBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion
import zakadabar.stack.util.PublicApi

/**
 * Query accounts. Requires the user to be logged in.
 */
@Serializable
class AccountList : QueryBo<List<AccountPublicBo>>, SelectOptionProvider<AccountPublicBo> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(AccountPublicBo.serializer()))

    @PublicApi
    @Suppress("UNCHECKED_CAST")
    override suspend fun asSelectOptions() =
        execute().map { it.accountId as EntityId<AccountPublicBo> to it.fullName }

    companion object : QueryBoCompanion(AccountPrivateBo.boNamespace)

}