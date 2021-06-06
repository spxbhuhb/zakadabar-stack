/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.content.backend.sub

import zakadabar.lib.blobs.backend.BlobBlBase
import zakadabar.lib.content.data.ContentBo
import zakadabar.lib.content.data.sub.ContentBlobBo
import zakadabar.stack.backend.authorize.Authorizer
import zakadabar.stack.backend.authorize.EmptyAuthorizer

class ContentBlobBl : BlobBlBase<ContentBlobBo, ContentBo>(
   ContentBlobBo::class,
   ContentBlobExposedPa()
) {
   override val authorizer: Authorizer<ContentBlobBo> = EmptyAuthorizer()
}
