/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.business

import kotlinx.serialization.Serializable
import zakadabar.core.data.EmptyEntityBo
import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion

@Serializable
class TestBlob(
    override var id: EntityId<TestBlob>,
    override var disposition: String,
    override var reference: EntityId<EmptyEntityBo>?,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<TestBlob, EmptyEntityBo> {

    companion object : BlobBoCompanion<TestBlob, EmptyEntityBo>("zkl-test-blob")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}