/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityBoCompanion
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
class BlobBo(

    override var id: EntityId<BlobBo>,
    var modifiedAt : Instant,
    var modifiedBy : EntityId<BaseBo>,
    var name: String,
    var mimeType: String,
    var size: Long,
    var linkedEntityNamespace : String,
    var linkedEntityId: EntityId<BaseBo>?

) : EntityBo<BlobBo> {

    companion object : EntityBoCompanion<BlobBo>("zkl-blob")

    override fun getBoNamespace() = BlobBo.boNamespace
    override fun comm() = BlobBo.comm

    override fun schema() = BoSchema {
        + ::name min 1 max 200
        + ::mimeType min 1 max 100
        + ::size min 0 max Long.MAX_VALUE
    }

}