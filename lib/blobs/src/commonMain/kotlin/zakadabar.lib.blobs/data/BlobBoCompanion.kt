/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import kotlinx.serialization.KSerializer

abstract class BlobBoCompanion<T : BlobBo<T>>(
    val boNamespace: String
) {

    private var _comm: BlobCommInterface<T>? = null

    private fun makeComm(): BlobCommInterface<T> {
        val nc = makeBlobComm(this)
        _comm = nc
        return nc
    }

    var comm: BlobCommInterface<T>
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

    abstract fun serializer(): KSerializer<T>

}