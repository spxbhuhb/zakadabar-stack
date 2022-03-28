/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.comm

import zakadabar.core.authorize.Executor
import zakadabar.core.comm.CommConfig
import zakadabar.core.comm.EntityCommInterface
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCreateState

interface BlobCommInterface<T : BlobBo<T, RT>, RT : EntityBo<RT>> : EntityCommInterface<T> {

    /**
     * Upload data for the given blob.
     *
     * @param   bo    The blob to upload the data for.
     * @param   data  ByteArray on JVM and Blob for JavaScript.
     *
     * @return  the [bo]
     */
    suspend fun upload(
        bo : T,
        data: Any,
        executor: Executor? = null,
        config : CommConfig? = null
    ) : T

    /**
     * Upload data for the given blob.
     *
     * @param   bo        The blob to upload the data for.
     * @param   data      ByteArray on JVM and Blob for JavaScript.
     * @param   callback  Function to execute as upload progresses.
     *
     * @return  the [bo]
     */
    suspend fun upload(
        bo : T,
        data: Any,
        executor: Executor? = null,
        config : CommConfig? = null,
        callback: (bo : T, state: BlobCreateState, uploaded: Long) -> Unit
    ) : T

    /**
     * Download the data of the blob into a ByteArray.
     */
    suspend fun download(id : EntityId<T>, executor: Executor?, config : CommConfig?) : ByteArray

    suspend fun byReference(
        reference : EntityId<RT>?,
        disposition : String? = null,
        executor: Executor? = null,
        config : CommConfig? = null
    ) : List<T>

}