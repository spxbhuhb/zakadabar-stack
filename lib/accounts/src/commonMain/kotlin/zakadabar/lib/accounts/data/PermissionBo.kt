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
class PermissionBo(

    override var id: EntityId<PermissionBo>,
    var name: String,
    var description: String

) : EntityBo<PermissionBo> {

    companion object : EntityBoCompanion<PermissionBo>("zkl-permission")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name min 1 max 50 blank false
        + ::description min 1
    }
}