/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.data

import kotlinx.serialization.Serializable
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.core.data.EntityId

/**
 * Blob (thumbnail, images, attachments) that belongs to a content.
 */
@Serializable
class AttachedBlobBo(
    override var id: EntityId<AttachedBlobBo>,
    override var reference: EntityId<ContentBo>?,
    override var disposition: String,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<AttachedBlobBo, ContentBo> {

    companion object : BlobBoCompanion<AttachedBlobBo, ContentBo>(ContentBo.boNamespace)

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}