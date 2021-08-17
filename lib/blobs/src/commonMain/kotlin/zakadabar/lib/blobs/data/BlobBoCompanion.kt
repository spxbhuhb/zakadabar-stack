/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import kotlinx.serialization.KSerializer
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.comm.BlobCommInterface
import zakadabar.lib.blobs.comm.makeBlobComm

abstract class BlobBoCompanion<T : BlobBo<T,RT>, RT : EntityBo<RT>>(
    val boNamespace: String
) {

    private var _comm: BlobCommInterface<T,RT>? = null

    private fun makeComm(): BlobCommInterface<T,RT> {
        val nc = makeBlobComm(this)
        _comm = nc
        return nc
    }

    var comm: BlobCommInterface<T, RT>
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }

    suspend fun read(id: EntityId<T>) = comm.read(id)

    suspend fun delete(id: EntityId<T>) = comm.delete(id)

    suspend fun all() = comm.all()

    suspend fun allAsMap() = comm.all().associateBy { it.id }

    abstract fun serializer(): KSerializer<T>

    suspend fun upload(bo : T, data: Any, callback: (bo : T, state: BlobCreateState, uploaded: Long) -> Unit) =
        comm.upload(bo, data, callback)

    suspend fun download(id : EntityId<T>) =
        comm.download(id)

    suspend fun byReference(reference : EntityId<RT>?, disposition : String? = null) =
        comm.byReference(reference, disposition)

    @Deprecated("EOL: 2021.7.1  -  use byReference instead", ReplaceWith("byReference(reference)"), level = DeprecationLevel.ERROR)
    suspend fun listByReference(reference : EntityId<RT>) =
        byReference(reference)

}