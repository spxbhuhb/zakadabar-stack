/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.attachment

import io.ktor.features.*
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.authorize.Executor
import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.business.BlobBlBase

class AttachmentBlobBl : BlobBlBase<AttachmentBlob, ExampleReferenceBo>(
   AttachmentBlob::class,
   AttachmentBlobPa()
) {
   override val authorizer by provider()

   override fun create(executor: Executor, bo: AttachmentBlob): AttachmentBlob {
      if (bo.size > 50000) throw BadRequestException("size limit is 50k bytes")
      if ((pa as AttachmentBlobPa).count() >= 1000) throw IllegalStateException("table limit reached")
      return super.create(executor, bo)
   }

   override fun writeContent(executor: Executor, blobId: EntityId<AttachmentBlob>, length : Long, bytes : ByteArray) = pa.withTransaction {
      if (length > 50000) throw BadRequestException("size limit is 50k bytes")
      super.writeContent(executor, blobId, length, bytes)
   }
}