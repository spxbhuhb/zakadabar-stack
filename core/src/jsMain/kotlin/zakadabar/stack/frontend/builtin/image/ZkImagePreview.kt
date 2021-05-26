/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.image

import zakadabar.stack.data.builtin.BlobBo
import zakadabar.stack.data.entity.BlobCreateState
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io

open class ZkImagePreview(
    var bo: BlobBo,
    var createState: BlobCreateState? = null,
    var progress: Long? = null,
    var size: Int = 200,
    var onDelete: suspend (preview: ZkImagePreview) -> Boolean = { false }
) : ZkElement() {

    override fun onCreate() {
        render()
    }

    fun update(bo: BlobBo, createState: BlobCreateState, progress: Long) {
        this.bo = bo
        this.createState = createState
        this.progress = progress
        render()
    }

    private fun render() = build {
        clear()

        when (createState) {
            BlobCreateState.Starting -> renderProgress()
            BlobCreateState.Progress -> renderProgress()
            BlobCreateState.Done -> renderImage()
            BlobCreateState.Error -> renderError()
            BlobCreateState.Abort -> renderAbort()
            null -> renderImage()
        }
    }

    private fun ZkElement.renderImage() {
        + column {
            + image(bo.url()) {
                with(buildPoint.style) {
                    height = "${size}px"
                    width = "${size}px"
                }

                on(buildPoint, "click") { _ ->
                    ZkFullScreenImageView(bo.url()) {
                        io {
                            val deleted = onDelete(this@ZkImagePreview)
                            if (deleted) it.hide()
                        }
                    }.show()
                }
            }
        }
    }

    private fun ZkElement.renderProgress() {
        + row {
            + "uploading: $progress / ${bo.size}"
        }
    }

    private fun ZkElement.renderError() {
        + "upload error"
    }

    private fun ZkElement.renderAbort() {
        + "aborted"
    }

}