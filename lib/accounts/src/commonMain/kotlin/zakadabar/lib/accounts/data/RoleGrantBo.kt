/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId

/**
 * A role grant, used to send the grant to the PA.
 */
@Serializable
class RoleGrantBo(
    var account : EntityId<AccountPrivateBo>,
    var role : EntityId<RoleBo>
) : BaseBo