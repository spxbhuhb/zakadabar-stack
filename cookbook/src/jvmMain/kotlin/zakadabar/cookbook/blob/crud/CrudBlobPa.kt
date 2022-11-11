/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.crud

import org.jetbrains.exposed.sql.selectAll
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.cookbook.entity.builtin.ExampleReferenceTable
import zakadabar.core.util.default
import zakadabar.lib.blobs.persistence.BlobExposedPa
import zakadabar.lib.blobs.persistence.BlobExposedTable

class CrudBlobPa : BlobExposedPa<CrudBlob, ExampleReferenceBo>(
    table = CrudBlobTable,
) {
    override fun newInstance() = default<CrudBlob> {  }

    fun count() = table.selectAll().count()

}

object CrudBlobTable : BlobExposedTable<CrudBlob, ExampleReferenceBo>(
    tableName = "cookbook_blob_crud",
    referenceTable = ExampleReferenceTable
)