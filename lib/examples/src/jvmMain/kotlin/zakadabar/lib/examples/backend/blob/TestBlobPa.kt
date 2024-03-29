/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.blob

import zakadabar.core.util.default
import zakadabar.lib.blobs.persistence.BlobExposedPa
import zakadabar.lib.blobs.persistence.BlobExposedTable
import zakadabar.lib.examples.backend.data.SimpleExampleExposedTableGen
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.lib.examples.data.TestBlob

class TestBlobExposedPa : BlobExposedPa<TestBlob, SimpleExampleBo>(
    table = TestBlobExposedTable,
) {
    override fun newInstance() = default<TestBlob> {  }
}

object TestBlobExposedTable : BlobExposedTable<TestBlob,SimpleExampleBo>(
    tableName = "test_blob",
    referenceTable = SimpleExampleExposedTableGen
)
