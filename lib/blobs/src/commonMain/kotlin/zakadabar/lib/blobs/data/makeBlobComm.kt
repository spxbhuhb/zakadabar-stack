/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

/**
 * Global function to make a [BlobCommInterface].
 */
expect fun <T : BlobBo<T,RT>, RT : EntityBo<RT>> makeBlobComm(companion: BlobBoCompanion<T,RT>): BlobCommInterface<T,RT>
