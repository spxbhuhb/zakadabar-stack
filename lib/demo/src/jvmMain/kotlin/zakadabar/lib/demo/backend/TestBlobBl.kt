/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.lib.blobs.backend.BlobBlBase
import zakadabar.lib.demo.data.TestBlob

class TestBlobBl : BlobBlBase<TestBlob, TestBo>(
    TestBlob::class,
    TestBlobExposedPa()
) {
    override val authorizer by provider()
}
