/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin.account

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.stack.data.query.QueryBo
import zakadabar.stack.data.query.QueryBoCompanion

@Serializable
class AccountByRole(
    val roleName: String
) : QueryBo<AccountPublicBo> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(AccountPublicBo.serializer()))

    companion object : QueryBoCompanion<AccountPublicBo>(AccountPublicBo.boNamespace)

}
