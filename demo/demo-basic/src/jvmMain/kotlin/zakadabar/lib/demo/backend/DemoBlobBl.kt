/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.demo.backend

import zakadabar.lib.blobs.business.BlobBlBase
import zakadabar.lib.demo.data.DemoBlob
import zakadabar.lib.demo.data.DemoBo

class DemoBlobBl : BlobBlBase<DemoBlob, DemoBo>(
    DemoBlob::class,
    DemoBlobExposedPa()
) {
    override val authorizer by provider()
}
