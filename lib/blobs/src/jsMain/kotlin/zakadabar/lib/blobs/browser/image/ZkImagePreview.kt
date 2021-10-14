/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.browser.image

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.io
import zakadabar.core.resource.css.px
import zakadabar.lib.blobs.browser.ZkBlobFieldEntry
import zakadabar.lib.blobs.browser.blobStyles
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCreateState
import zakadabar.lib.blobs.data.url

open class ZkImagePreview<BT : BlobBo<BT, *>>(
    bo: BT,
    createState: BlobCreateState? = null,
    progress: Long? = null,
    size: Int = 200,
    private val showMeta: Boolean = false,
    onDelete: suspend (preview: ZkBlobFieldEntry<BT>) -> Boolean = { false }
) : ZkBlobFieldEntry<BT>(
    bo, createState, progress, size, onDelete
) {

    override fun render() {
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

            + image(bo.url) {
                with(buildPoint.style) {
                    if (height > width) {
                        height = size.px
                        width = "auto"
                    } else {
                        height = "auto"
                        width = size.px
                    }
                }

                on(buildPoint, "click") { _ ->
                    ZkFullScreenImageView(bo.url) {
                        io {
                            val deleted = onDelete(this@ZkImagePreview)
                            if (deleted) it.hide()
                        }
                    }.show()
                }
            }

            if (showMeta) {
                + div(blobStyles.imageName) {
                    + bo.name
                }
                + div(blobStyles.imageMimeType) {
                    + bo.mimeType
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