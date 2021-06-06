/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data.sub

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema


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
class ContentStatusBo(

    override var id : EntityId<ContentStatusBo>,
    var name : String

) : EntityBo<ContentStatusBo> {

    companion object : EntityBoCompanion<ContentStatusBo>("zkl-content-status")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name blank false min 2 max 100
    }

}