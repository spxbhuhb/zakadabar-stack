/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.EntityCommInterface
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityBoCompanion
import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.comm.BlobCommInterface
import zakadabar.lib.blobs.comm.makeBlobCommV2

abstract class BlobBoCompanionV2<T : BlobBo<T, RT>, RT : EntityBo<RT>>(
    boNamespace: String,
    commConfig: CommConfig? = null
) : EntityBoCompanion<T>(
    boNamespace, commConfig
) {

    private var _comm: BlobCommInterface<T, RT>? = null

    private fun makeComm(): BlobCommInterface<T, RT> {
        val nc = makeBlobCommV2(this)
        _comm = nc
        return nc
    }

    override var comm: EntityCommInterface<T>
        get() = _comm ?: makeComm()
        set(value) {
            @Suppress("UNCHECKED_CAST")
            _comm = value as BlobCommInterface<T, RT>
        }

    var blobComm: BlobCommInterface<T, RT>
        get() = _comm ?: makeComm()
        set(value) {
            _comm = value
        }


    suspend fun upload(bo: T, data: Any, executor: Executor? = null, config: CommConfig? = null, callback: (bo: T, state: BlobCreateState, uploaded: Long) -> Unit) =
        blobComm.upload(bo, data, executor, config, callback)

    suspend fun download(id: EntityId<T>, executor: Executor? = null, config: CommConfig? = null) =
        blobComm.download(id, executor, config)

    suspend fun byReference(reference: EntityId<RT>?, disposition: String? = null, executor: Executor? = null, config: CommConfig? = null) =
        blobComm.byReference(reference, disposition, executor, config)

}