/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
class RoleGrantBo(

    override var id: EntityId<RoleGrantBo>,
    var principal: EntityId<PrincipalBo>,
    var role: EntityId<RoleBo>

) : EntityBo<RoleGrantBo> {

    companion object : EntityBoCompanion<RoleGrantBo>("role-grant")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::principal
        + ::role
    }

}