/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema


/**
 * Business Object of ContentStatusBo.
 * 
 * Generated with Bender at 2021-06-05T02:07:27.684Z.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on 
 * the backend.
 */
@Serializable
class StatusBo(

    override var id : EntityId<StatusBo>,
    var name : String

) : EntityBo<StatusBo> {

    companion object : EntityBoCompanion<StatusBo>("zkl-content-status")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name blank false min 2 max 100
    }

}