/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityId
import zakadabar.stack.data.schema.BoSchema

interface BlobBo<T : BlobBo<T,RT>, RT : EntityBo<RT>> : EntityBo<T> {

    override var id: EntityId<T>
    var reference: EntityId<RT>?
    var disposition: String
    var name: String
    var mimeType: String
    var size: Long

    override fun comm() : BlobCommInterface<T,RT>

    override fun schema() = BoSchema {
        + ::disposition max 200
        + ::name min 1 max 200
        + ::mimeType min 1 max 100
        + ::size min 0 max Int.MAX_VALUE.toLong()
    }

}