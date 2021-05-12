/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.image

import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.data.record.BlobCreateState
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.io

open class ZkImagePreview(
    var dto: BlobDto,
    var createState: BlobCreateState? = null,
    var progress: Long? = null,
    var size: Int = 200,
    var onDelete: suspend (preview: ZkImagePreview) -> Boolean = { false }
) : ZkElement() {

    override fun onCreate() {
        render()
    }

    fun update(dto: BlobDto, createState: BlobCreateState, progress: Long) {
        this.dto = dto
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
            + image(dto.url()) {
                with(buildPoint.style) {
                    height = "${size}px"
                    width = "${size}px"
                }

                on(buildPoint, "click") { _ ->
                    ZkFullScreenImageView(dto.url()) {
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
            + "uploading: $progress / ${dto.size}"
        }
    }

    private fun ZkElement.renderError() {
        + "upload error"
    }

    private fun ZkElement.renderAbort() {
        + "aborted"
    }

}