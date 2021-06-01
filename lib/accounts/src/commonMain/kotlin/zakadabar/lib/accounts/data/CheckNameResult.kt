/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

@Serializable
class CheckNameResult(

    var accountName: String,
    var accountId: EntityId<AccountPrivateBo>?

) : BaseBo