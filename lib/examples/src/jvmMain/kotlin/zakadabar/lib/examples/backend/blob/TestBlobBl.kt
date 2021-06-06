/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.blob

import zakadabar.lib.blobs.backend.BlobBlBase
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.lib.examples.data.TestBlob
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.UnsafeAuthorizer

class TestBlobBl : BlobBlBase<TestBlob, SimpleExampleBo>(
     TestBlob::class,
     TestBlobExposedPa()
) {
     override val authorizer: Authorizer<TestBlob> = UnsafeAuthorizer()
}
