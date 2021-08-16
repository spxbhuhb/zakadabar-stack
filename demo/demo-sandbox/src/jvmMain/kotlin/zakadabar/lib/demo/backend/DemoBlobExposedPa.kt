/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.core.util.default
import zakadabar.lib.blobs.persistence.BlobExposedPa
import zakadabar.lib.blobs.persistence.BlobExposedTable
import zakadabar.lib.demo.data.DemoBlob
import zakadabar.lib.demo.data.DemoBo

class DemoBlobExposedPa : BlobExposedPa<DemoBlob, DemoBo>(
    table = DemoBlobExposedPaTable,
) {
    override fun newInstance() = default<DemoBlob> {  }
}

object DemoBlobExposedPaTable : BlobExposedTable<DemoBlob, DemoBo>(
    tableName = "demo_blob",
    referenceTable = DemoExposedTableGen
)
