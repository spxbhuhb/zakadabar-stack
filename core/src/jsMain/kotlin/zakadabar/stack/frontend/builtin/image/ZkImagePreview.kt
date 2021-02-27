/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.image

import zakadabar.stack.data.builtin.BlobDto
import zakadabar.stack.data.record.BlobCreateState
import zakadabar.stack.frontend.elements.ZkElement

open class ZkImagePreview(
    var dto: BlobDto,
    var state: BlobCreateState? = null,
    var progress: Long? = null,
    var size: Int = 200,
    var onDelete: (preview: ZkImagePreview) -> Boolean = { false }
) : ZkElement() {

    override fun init() = build {
        render()
    }

    fun update(dto: BlobDto, state: BlobCreateState, progress: Long) {
        this.dto = dto
        this.state = state
        this.progress = progress
        cleanup()
        render()
    }

    private fun render() = build {
        when (state) {
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
                with(currentElement.style) {
                    height = "${size}px"
                    width = "${size}px"
                }

                on(currentElement, "click") { _ ->
                    ZkFullScreenImageView(dto.url()) {
                        val deleted = onDelete(this@ZkImagePreview)
                        if (deleted) it.hide()
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