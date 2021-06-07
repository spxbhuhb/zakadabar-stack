/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend

import zakadabar.lib.blobs.backend.BlobBlBase
import zakadabar.lib.content.data.ContentBlobBo
import zakadabar.lib.content.data.ContentCommonBo

class ContentBlobBl : BlobBlBase<ContentBlobBo, ContentCommonBo>(
   ContentBlobBo::class,
   ContentBlobExposedPa()
) {
   override val authorizer by provider()
}
