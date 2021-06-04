/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.data

import kotlinx.serialization.Serializable
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityId

@Serializable
class TestBlob(
    override var id: EntityId<TestBlob>,
    override var reference: EntityId<out BaseBo>?,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<TestBlob> {

    companion object : BlobBoCompanion<TestBlob>("test-blob")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}
