/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.blobs.backend.BlobExposedPa
import zakadabar.lib.blobs.backend.BlobExposedTable
import zakadabar.lib.content.data.AttachedBlobBo
import zakadabar.lib.content.data.ContentBo
import zakadabar.core.util.default

class AttachedBlobExposedPa : BlobExposedPa<AttachedBlobBo, ContentBo>(
    table = AttachedBlobExposedTable
) {
    override fun newInstance() = default<AttachedBlobBo> {  }
}

object AttachedBlobExposedTable : BlobExposedTable<AttachedBlobBo, ContentBo>(
    tableName = "content_blob",
    referenceTable = ContentExposedTable
)
