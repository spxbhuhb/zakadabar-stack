/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.image

import kotlinx.serialization.Serializable
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion

@Serializable
class ImageBlob(
    override var id: EntityId<ImageBlob>,
    override var disposition: String,
    override var reference: EntityId<ExampleReferenceBo>?,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<ImageBlob, ExampleReferenceBo> {

   companion object : BlobBoCompanion<ImageBlob, ExampleReferenceBo>("zkc-blob-image")

   override fun getBoNamespace() = boNamespace
   override fun comm() = comm

}
