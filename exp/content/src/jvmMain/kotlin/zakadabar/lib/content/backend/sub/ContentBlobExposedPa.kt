/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend.sub

import zakadabar.lib.blobs.backend.BlobExposedPa
import zakadabar.lib.blobs.backend.BlobExposedTable
import zakadabar.lib.content.backend.ContentExposedTableGen
import zakadabar.lib.content.data.ContentBo
import zakadabar.lib.content.data.sub.ContentBlobBo
import zakadabar.stack.backend.util.default

class ContentBlobExposedPa : BlobExposedPa<ContentBlobBo, ContentBo>(
    table = ContentBlobExposedTable,
) {
    override fun newInstance() = default<ContentBlobBo> {  }
}

object ContentBlobExposedTable : BlobExposedTable<ContentBlobBo, ContentBo>(
    tableName = "content_blob",
    referenceTable = ContentExposedTableGen
)
