/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.QueryBo
import zakadabar.core.data.QueryBoCompanion

@Serializable
class AuthProviderList : QueryBo<List<AuthProviderBo>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(AuthProviderBo.serializer()))

    companion object : QueryBoCompanion(AuthProviderBo.boNamespace)

}