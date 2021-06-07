/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.blobs.backend.BlobExposedPa
import zakadabar.lib.blobs.backend.BlobExposedTable
import zakadabar.lib.content.data.ContentBlobBo
import zakadabar.lib.content.data.ContentCommonBo
import zakadabar.stack.backend.util.default

class ContentBlobExposedPa : BlobExposedPa<ContentBlobBo, ContentCommonBo>(
    table = ContentBlobExposedTable,
) {
    override fun newInstance() = default<ContentBlobBo> {  }
}

object ContentBlobExposedTable : BlobExposedTable<ContentBlobBo, ContentCommonBo>(
    tableName = "content_blob",
    referenceTable = ContentCommonExposedTableGen
)
