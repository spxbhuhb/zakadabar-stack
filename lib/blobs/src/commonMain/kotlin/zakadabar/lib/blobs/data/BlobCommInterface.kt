/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.data

import zakadabar.stack.data.entity.EntityBo
import zakadabar.stack.data.entity.EntityCommInterface
import zakadabar.stack.data.entity.EntityId

interface BlobCommInterface<T : BlobBo<T,RT>, RT : EntityBo<RT>> : EntityCommInterface<T> {

    suspend fun upload(bo : T, data: Any, callback: (bo : T, state: BlobCreateState, uploaded: Long) -> Unit)

    suspend fun listByReference(reference : EntityId<RT>, disposition : String? = null) : List<T>

}