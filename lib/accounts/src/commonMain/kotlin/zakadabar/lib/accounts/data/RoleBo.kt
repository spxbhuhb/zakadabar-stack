/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

/**
 * A role which has some business specific meaning.
 */
@Serializable
class RoleBo(

    override var id: EntityId<RoleBo>,
    var name: String,
    var description: String

) : EntityBo<RoleBo> {

    companion object : EntityBoCompanion<RoleBo>("role")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name min 1 max 50 blank false
        + ::description
    }
}