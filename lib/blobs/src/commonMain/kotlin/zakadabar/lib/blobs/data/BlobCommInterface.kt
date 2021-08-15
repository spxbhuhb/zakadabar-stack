/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import zakadabar.core.comm.EntityCommInterface
import zakadabar.core.data.EntityBo
import zakadabar.core.data.EntityId

interface BlobCommInterface<T : BlobBo<T,RT>, RT : EntityBo<RT>> : EntityCommInterface<T> {

    /**
     * Upload data for the given blob.
     *
     * @param   bo    The blob to upload the data for.
     * @param   data  ByteArray on JVM and Blob for JavaScript.
     *
     * @return  the [bo]
     */
    suspend fun upload(bo : T, data: Any) : T

    /**
     * Upload data for the given blob.
     *
     * @param   bo        The blob to upload the data for.
     * @param   data      ByteArray on JVM and Blob for JavaScript.
     * @param   callback  Function to execute as upload progresses.
     *
     * @return  the [bo]
     */
    suspend fun upload(bo : T, data: Any, callback: (bo : T, state: BlobCreateState, uploaded: Long) -> Unit) : T

    /**
     * Download the data of the blob into a ByteArray.
     */
    suspend fun download(id : EntityId<T>) : ByteArray

    suspend fun byReference(reference : EntityId<RT>?, disposition : String? = null) : List<T>

    @Deprecated("EOL: 2021.7.1  -  use byReference instead", ReplaceWith("byReference(reference)"), level = DeprecationLevel.ERROR)
    suspend fun listByReference(reference : EntityId<RT>, disposition : String? = null) =
        byReference(reference, disposition)

}