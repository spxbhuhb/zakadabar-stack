/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.authorize.AccountPublicBo
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

@Deprecated("EOL: 2021.7.1  -  use AccountsByRole instead", ReplaceWith("AccountsByRole(roleName)"), level = DeprecationLevel.ERROR)
class AccountByRole(roleName: String) : AccountsByRole(roleName)

@Serializable
open class AccountsByRole(
    val roleName: String
) : QueryBo<List<AccountPublicBo>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(AccountPublicBo.serializer()))

    companion object : QueryBoCompanion(RoleBo.boNamespace)

}
