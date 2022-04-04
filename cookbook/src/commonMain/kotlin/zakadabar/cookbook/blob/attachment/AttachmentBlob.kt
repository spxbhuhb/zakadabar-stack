/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.attachment

import kotlinx.serialization.Serializable
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion

@Serializable
class AttachmentBlob(
   override var id: EntityId<AttachmentBlob>,
   override var disposition: String,
   override var reference: EntityId<ExampleReferenceBo>?,
   override var name: String,
   override var mimeType: String,
   override var size: Long
) : BlobBo<AttachmentBlob, ExampleReferenceBo> {

   companion object : BlobBoCompanion<AttachmentBlob, ExampleReferenceBo>("zkc-blob-attachment")

   override fun getBoNamespace() = boNamespace
   override fun comm() = comm

}
