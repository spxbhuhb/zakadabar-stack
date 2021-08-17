/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.core.util.default
import zakadabar.lib.blobs.persistence.BlobExposedPa
import zakadabar.lib.blobs.persistence.BlobExposedTable
import zakadabar.lib.content.data.AttachedBlobBo
import zakadabar.lib.content.data.ContentBo

class AttachedBlobExposedPa : BlobExposedPa<AttachedBlobBo, ContentBo>(
    table = AttachedBlobExposedTable
) {
    override fun newInstance() = default<AttachedBlobBo> {  }
}

object AttachedBlobExposedTable : BlobExposedTable<AttachedBlobBo, ContentBo>(
    tableName = "content_blob",
    referenceTable = ContentExposedTable
)
