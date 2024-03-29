/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.examples.backend.blob

import zakadabar.core.authorize.BusinessLogicAuthorizer
import zakadabar.core.authorize.UnsafeAuthorizer
import zakadabar.lib.blobs.business.BlobBlBase
import zakadabar.lib.examples.data.SimpleExampleBo
import zakadabar.lib.examples.data.TestBlob

class TestBlobBl : BlobBlBase<TestBlob, SimpleExampleBo>(
     TestBlob::class,
     TestBlobExposedPa()
) {
     override val authorizer: BusinessLogicAuthorizer<TestBlob> = UnsafeAuthorizer()
}
