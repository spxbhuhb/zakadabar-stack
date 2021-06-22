/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.backend

import org.jetbrains.exposed.dao.id.LongIdTable
import zakadabar.stack.backend.util.default

class TestBlobExposedPa : BlobExposedPa<TestBlob>(
    table = TestBlobExposedTable,
) {
    override fun newInstance() = default<TestBlob> {  }
}

object TestBlobExposedTable : BlobExposedTable<TestBlob>(
    tableName = "test_blob",
    referenceTable = TestBlobReferenceExposedTable
)

object TestBlobReferenceExposedTable : LongIdTable(
    "test_blob_ref",
)
