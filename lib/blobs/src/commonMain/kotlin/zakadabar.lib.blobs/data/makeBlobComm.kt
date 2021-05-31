/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import zakadabar.stack.data.entity.EntityBo

/**
 * Global function to make a [BlobCommInterface].
 */
expect fun <T : BlobBo<T>> makeBlobComm(companion: BlobBoCompanion<T>): BlobCommInterface<T>
