/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.frontend

import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCreateState
import zakadabar.core.frontend.builtin.ZkElement

abstract class ZkBlobFieldEntry<BT : BlobBo<BT, *>>(
    var bo: BT,
    var createState: BlobCreateState? = null,
    var progress: Long? = null,
    var size: Int = 200,
    var onDelete: suspend (preview: ZkBlobFieldEntry<BT>) -> Boolean = { false }
) : ZkElement() {

    override fun onCreate() {
        render()
    }

    fun update(bo: BT, createState: BlobCreateState, progress: Long) {
        this.bo = bo
        this.createState = createState
        this.progress = progress
        render()
    }

    abstract fun render()

}