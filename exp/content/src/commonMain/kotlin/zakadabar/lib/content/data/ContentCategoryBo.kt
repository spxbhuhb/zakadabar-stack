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
 * Business Object of ContentCategoryBo.
 * 
 * Generated with Bender at 2021-06-05T02:31:08.494Z.
 *
 * Please do not implement business logic in this class. If you add fields,
 * please check the frontend table and form, and also the persistence API on 
 * the backend.
 */
@Serializable
class ContentCategoryBo(

    override var id : EntityId<ContentCategoryBo>,
    var name : String

) : EntityBo<ContentCategoryBo> {

    companion object : EntityBoCompanion<ContentCategoryBo>("zkl-content-category")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

    override fun schema() = BoSchema {
        + ::id
        + ::name blank false min 2 max 100
    }

}