/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

actual fun <T : BlobBo<T>>  makeBlobComm(companion :BlobBoCompanion<T>): BlobCommInterface<T> {
    return BlobComm(companion.boNamespace, companion.serializer())
}