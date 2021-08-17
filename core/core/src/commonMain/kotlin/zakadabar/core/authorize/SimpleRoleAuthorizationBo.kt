/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.authorize

import kotlinx.serialization.Serializable
import zakadabar.core.data.BaseBo
import zakadabar.core.data.EntityId

@Serializable
class SimpleRoleAuthorizationBo(
    var list: EntityId<out BaseBo>?,
    var read: EntityId<out BaseBo>?,
    var create: EntityId<out BaseBo>?,
    var update: EntityId<out BaseBo>?,
    var delete: EntityId<out BaseBo>?,
    var query: EntityId<out BaseBo>?,
    var action: EntityId<out BaseBo>?
) : BaseBo