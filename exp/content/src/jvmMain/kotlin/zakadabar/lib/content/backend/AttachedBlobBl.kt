/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.blobs.business.BlobBlBase
import zakadabar.lib.content.data.AttachedBlobBo
import zakadabar.lib.content.data.ContentBo

class AttachedBlobBl : BlobBlBase<AttachedBlobBo, ContentBo>(
   AttachedBlobBo::class,
   AttachedBlobExposedPa()
) {
   override val authorizer by provider()
}
