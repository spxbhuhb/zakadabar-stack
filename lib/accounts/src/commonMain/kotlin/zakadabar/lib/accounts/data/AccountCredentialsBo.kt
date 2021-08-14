/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.lib.accounts.data

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.builtin.misc.Secret
import zakadabar.core.data.entity.EntityId
import zakadabar.core.data.schema.BoSchema

/**
 * Account credentials. Each credential has a type that defines the context
 * the given credential is used for, like "password".
 *
 * @property  accountId   The id of the [AccountPrivateBo] BO this credentials belong to.
 * @property  type        Type of the credential. Value set depends on the
 *                        authentication modules used.
 * @property  value       Value of the credential. Structure of this value
 *                        depends on [type].
 * @property  expiration  Expiration of this credential, if any.
 */
@Serializable
open class AccountCredentialsBo(

    var accountId: EntityId<AccountPrivateBo>,

    var type : String,
    var value : Secret,
    var expiration : Instant?,

) : BaseBo {

    override fun schema() = BoSchema {
        + ::accountId

        + ::type blank false max 50
        + ::value
        + ::expiration
    }
}