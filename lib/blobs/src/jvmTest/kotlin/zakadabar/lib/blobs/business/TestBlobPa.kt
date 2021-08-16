/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.business

import org.jetbrains.exposed.dao.id.LongIdTable
import zakadabar.core.data.EmptyEntityBo
import zakadabar.core.util.default
import zakadabar.lib.blobs.persistence.BlobExposedPa
import zakadabar.lib.blobs.persistence.BlobExposedTable

class TestBlobExposedPa : BlobExposedPa<TestBlob, EmptyEntityBo>(
    table = TestBlobExposedTable,
) {
    override fun newInstance() = default<TestBlob> {  }
}

object TestBlobExposedTable : BlobExposedTable<TestBlob, EmptyEntityBo>(
    tableName = "test_blob",
    referenceTable = TestBlobReferenceExposedTable
)

object TestBlobReferenceExposedTable : LongIdTable(
    "test_blob_ref",
)
