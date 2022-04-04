/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.image

import org.jetbrains.exposed.sql.selectAll
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceTable
import zakadabar.core.util.default
import zakadabar.lib.blobs.persistence.BlobExposedPa
import zakadabar.lib.blobs.persistence.BlobExposedTable

class ImageBlobPa : BlobExposedPa<ImageBlob, ExampleReferenceBo>(
   table = TestBlobTable,
) {
   override fun newInstance() = default<ImageBlob> {  }

   fun count() = table.selectAll().count()

}

object TestBlobTable : BlobExposedTable<ImageBlob, ExampleReferenceBo>(
   tableName = "cookbook_blob_image",
   referenceTable = ExampleReferenceTable
)