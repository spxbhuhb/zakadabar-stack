/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.lib.blobs.backend.BlobExposedPa
import zakadabar.lib.blobs.backend.BlobExposedTable
import zakadabar.lib.demo.data.DemoBlob
import zakadabar.lib.demo.data.DemoBo
import zakadabar.core.util.default

class DemoBlobExposedPa : BlobExposedPa<DemoBlob, DemoBo>(
    table = DemoBlobExposedPaTable,
) {
    override fun newInstance() = default<DemoBlob> {  }
}

object DemoBlobExposedPaTable : BlobExposedTable<DemoBlob, DemoBo>(
    tableName = "demo_blob",
    referenceTable = DemoExposedTableGen
)
