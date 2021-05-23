/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.data.builtin

import kotlinx.serialization.Serializable
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityCommInterface
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

@Serializable
data class BlobBo(

    override var id: EntityId<BlobBo>,
    var entityId: EntityId<BaseBo>?,
    var namespace: String,
    var name: String,
    var type: String,
    var size: Long

) : EntityBo<BlobBo> {

    override fun schema() = BoSchema {
        + ::name min 1 max 200
        + ::type min 1 max 100
        + ::size min 0 max Long.MAX_VALUE
    }

    override fun getBoNamespace() = namespace

    override fun comm(): EntityCommInterface<BlobBo> {
        throw IllegalStateException("comm of BlobBo should not be used directly")
    }

    /**
     * Get an URL for the a BLOB.
     */
    fun url() = "/api/$namespace/blob/content/$id"

}