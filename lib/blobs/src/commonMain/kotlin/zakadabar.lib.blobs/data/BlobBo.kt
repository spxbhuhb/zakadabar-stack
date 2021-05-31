/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

interface BlobBo<T : BlobBo<T>> : EntityBo<T> {

    override var id: EntityId<T>
    var reference: EntityId<out BaseBo>?
    var name: String
    var mimeType: String
    var size: Long

    override fun comm() : BlobCommInterface<T>

    override fun schema() = BoSchema {
        + ::name min 1 max 200
        + ::mimeType min 1 max 100
        + ::size min 0 max Int.MAX_VALUE.toLong()
    }

}