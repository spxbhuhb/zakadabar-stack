/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import zakadabar.core.data.BaseBo
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.query.QueryBo
import zakadabar.core.data.query.QueryBoCompanion

/**
 * List accounts for management. Caller must have the security officer role.
 */
@Serializable
class AccountListSecure : QueryBo<List<AccountListSecureEntry>> {

    override suspend fun execute() = comm.query(this, serializer(), ListSerializer(AccountListSecureEntry.serializer()))

    companion object : QueryBoCompanion(AccountPrivateBo.boNamespace)

}

@Serializable
class AccountListSecureEntry(

    var accountId: EntityId<AccountPrivateBo>,
    var accountName: String,
    var fullName: String,
    var email: String,
    var phone: String?,
    var locale : String,
    var validated : Boolean,
    var locked : Boolean,
    var expired : Boolean,
    var anonymized : Boolean

) : BaseBo