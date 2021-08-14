/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.data

import kotlinx.serialization.Serializable
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobBoCompanion
import zakadabar.core.data.entity.EntityId

@Serializable
class TestBlob(
    override var id: EntityId<TestBlob>,
    override var reference: EntityId<SimpleExampleBo>?,
    override var disposition: String,
    override var name: String,
    override var mimeType: String,
    override var size: Long
) : BlobBo<TestBlob, SimpleExampleBo> {

    companion object : BlobBoCompanion<TestBlob, SimpleExampleBo>("zkl-test-blob")

    override fun getBoNamespace() = boNamespace
    override fun comm() = comm

}