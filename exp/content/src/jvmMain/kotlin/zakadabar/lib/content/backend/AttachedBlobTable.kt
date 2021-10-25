/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.blobs.persistence.BlobExposedTable
import zakadabar.lib.content.data.AttachedBlobBo
import zakadabar.lib.content.data.ContentBo

object AttachedBlobTable : BlobExposedTable<AttachedBlobBo, ContentBo>(
    tableName = "content_blob",
    referenceTable = ContentTable
)