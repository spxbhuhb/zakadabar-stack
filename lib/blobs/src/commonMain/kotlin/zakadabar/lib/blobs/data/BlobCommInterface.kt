/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import zakadabar.stack.data.BaseBo
import zakadabar.stack.data.entity.EntityCommInterface
import zakadabar.stack.data.entity.EntityId

interface BlobCommInterface<T : BlobBo<T>> : EntityCommInterface<T> {

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

    suspend fun byReference(reference : EntityId<out BaseBo>?) : List<T>

    @Deprecated("EOL: 2021.7.1  --  use byReference instead", ReplaceWith("byReference(reference)"))
    suspend fun listByReference(reference : EntityId<out BaseBo>) = byReference(reference)

}