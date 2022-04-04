/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.attachment

import org.jetbrains.exposed.sql.selectAll
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceTable
import zakadabar.core.util.default
import zakadabar.lib.blobs.persistence.BlobExposedPa
import zakadabar.lib.blobs.persistence.BlobExposedTable

class AttachmentBlobPa : BlobExposedPa<AttachmentBlob, ExampleReferenceBo>(
   table = AttachmentBlobTable,
) {
   override fun newInstance() = default<AttachmentBlob> {  }

   fun count() = table.selectAll().count()

}

object AttachmentBlobTable : BlobExposedTable<AttachmentBlob, ExampleReferenceBo>(
   tableName = "cookbook_blob_attachment",
   referenceTable = ExampleReferenceTable
)