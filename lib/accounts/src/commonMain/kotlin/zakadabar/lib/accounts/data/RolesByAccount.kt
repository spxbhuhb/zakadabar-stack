/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

@Serializable
class RolesByAccount(
    val accountId: EntityId<AccountPrivateBo>
) : QueryBo<List<RoleGrantBo>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(RoleGrantBo.serializer()))

    companion object : QueryBoCompanion(RoleBo.boNamespace)

}