/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId

// using the permission name instead of the id, caused by convenience reasons
@Serializable
class SimplePermissionAuthorizationBo(
    var list: String?,
    var read: String?,
    var create: String?,
    var update: String?,
    var delete: String?,
    var query: String?,
    var action: String?
) : BaseBo