/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.backend

import zakadabar.core.data.EmptyEntityBo

class TestBlobBl : BlobBlBase<TestBlob, EmptyEntityBo>(
     TestBlob::class,
     TestBlobExposedPa()
) {
     override val authorizer by provider()
}
