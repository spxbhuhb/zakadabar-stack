/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema


/**
 * Business Object of ContentTextBo.
 * 
 * Generated with Bender at 2021-06-07T02:56:36.421Z.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on 
 * the backend.
 */
@Serializable
class ContentTextBo(

    override var id : EntityId<ContentTextBo>,
    var content : EntityId<ContentCommonBo>,
    var type : ContentTextType,
    var value : String

) : EntityBo<ContentTextBo> {

    companion object : EntityBoCompanion<ContentTextBo>("zkl-content-text")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::content
        + ::type 
        + ::value 
    }

}