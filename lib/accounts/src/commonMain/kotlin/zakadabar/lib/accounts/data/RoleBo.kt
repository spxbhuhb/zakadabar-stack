/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.accounts.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema

/**
 * A role which has some business specific meaning.
 */
@Serializable
class RoleBo(

    override var id: EntityId<RoleBo>,
    var name: String,
    var description: String

) : EntityBo<RoleBo> {

    companion object : EntityBoCompanion<RoleBo>("zkl-role")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name min 1 max 50 blank false
        + ::description
    }
}