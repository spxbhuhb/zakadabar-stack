/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.stack.data.entity.EntityId

@Serializable
class ContentBlobBo(
    override var id: EntityId<ContentBlobBo>,
    override var reference: EntityId<ContentCommonBo>?,
    override var disposition: String,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<ContentBlobBo, ContentCommonBo> {

    companion object : BlobBoCompanion<ContentBlobBo, ContentCommonBo>(ContentCommonBo.boNamespace)

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}