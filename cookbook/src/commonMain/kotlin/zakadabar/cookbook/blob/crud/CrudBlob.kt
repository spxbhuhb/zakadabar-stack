/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.cookbook.blob.crud

import kotlinx.serialization.Serializable
import zakadabar.cookbook.entity.builtin.ExampleReferenceBo
import zakadabar.core.data.EntityId
import zakadabar.core.schema.BoSchema
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanionV2

@Serializable
class CrudBlob(
    override var id: EntityId<CrudBlob>,
    override var disposition: String,
    override var reference: EntityId<ExampleReferenceBo>?,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<CrudBlob, ExampleReferenceBo> {

   companion object : BlobBoCompanionV2<CrudBlob, ExampleReferenceBo>("zkc-blob-crud")

   override fun getBoNamespace() = boNamespace
   override fun comm() = blobComm

    override fun schema() = BoSchema {
        + ::id
        + ::disposition
        + ::reference
        + ::name
        + ::mimeType
        + ::size
    }
}
