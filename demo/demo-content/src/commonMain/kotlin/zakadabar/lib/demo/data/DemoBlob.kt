/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.data

import kotlinx.serialization.Serializable
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.stack.data.entity.EntityId

@Serializable
class DemoBlob(
    override var id: EntityId<DemoBlob>,
    override var reference: EntityId<DemoBo>?,
    override var disposition: String,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<DemoBlob, DemoBo> {

    companion object : BlobBoCompanion<DemoBlob,DemoBo>("test-blob")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}
