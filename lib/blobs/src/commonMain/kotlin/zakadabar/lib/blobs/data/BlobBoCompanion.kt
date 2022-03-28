/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import kotlinx.serialization.KSerializer
import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.core.util.use
import zakadabar.lib.blobs.comm.BlobCommInterface
import zakadabar.lib.blobs.comm.makeBlobComm

abstract class BlobBoCompanion<T : BlobBo<T,RT>, RT : EntityBo<RT>>(
    val boNamespace: String,
    commConfig : CommConfig? = null
) {

    private var _comm: BlobCommInterface<T,RT>? = null

    var commConfig = commConfig
        get() = CommConfig.configLock.use { field }
        set(value) = CommConfig.configLock.use { field = value }

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

    suspend fun read(id: EntityId<T>, executor: Executor? = null, config: CommConfig? = null) = comm.read(id, executor, config)

    suspend fun delete(id: EntityId<T>, executor: Executor? = null, config: CommConfig? = null) = comm.delete(id, executor, config)

    suspend fun all(executor: Executor? = null, config: CommConfig? = null) = comm.all(executor, config)

    suspend fun allAsMap(executor: Executor? = null, config: CommConfig? = null) = comm.all(executor, config).associateBy { it.id }

    abstract fun serializer(): KSerializer<T>

    suspend fun upload(bo : T, data: Any, executor: Executor? = null, config: CommConfig? = null, callback: (bo : T, state: BlobCreateState, uploaded: Long) -> Unit) =
        comm.upload(bo, data, executor, config, callback)

    suspend fun download(id : EntityId<T>, executor: Executor? = null, config: CommConfig? = null) =
        comm.download(id, executor, config)

    suspend fun byReference(reference : EntityId<RT>?, disposition : String? = null, executor: Executor? = null, config: CommConfig? = null) =
        comm.byReference(reference, disposition, executor, config)

}