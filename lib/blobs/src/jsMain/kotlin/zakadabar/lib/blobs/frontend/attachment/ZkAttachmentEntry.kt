/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.frontend.attachment

import kotlinx.browser.window
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCreateState
import zakadabar.lib.blobs.data.url
import zakadabar.lib.blobs.frontend.blobStyles
import zakadabar.lib.blobs.resources.blobStrings
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons

open class ZkAttachmentEntry<BT : BlobBo<BT, *>>(
    var bo: BT,
    var createState: BlobCreateState? = null,
    var progress: Long? = null,
    var size: Int = 200,
    var onDelete: suspend (preview: ZkAttachmentEntry<BT>) -> Boolean = { false }
) : ZkElement() {

    override fun onCreate() {
        + blobStyles.attachmentEntry
        render()
    }

    fun update(bo: BT, createState: BlobCreateState, progress: Long) {
        this.bo = bo
        this.createState = createState
        this.progress = progress
        render()
    }

    private fun render() = build {
        clear()

        + div { + bo.name }
        + div { + bo.size.toString() }
        + div { + bo.mimeType }

        when (createState) {
            BlobCreateState.Starting -> + div { + blobStrings.starting }
            BlobCreateState.Progress -> + div { + blobStrings.uploading }
            BlobCreateState.Error -> + div { + blobStrings.uploadError }
            BlobCreateState.Abort -> + div { + blobStrings.uploadAbort }
            else -> {
                + ZkButton(
                    iconSource = ZkIcons.fileDownload,
                    flavour = ZkFlavour.Info,
                    url = bo.url(),
                    onClick = {  }
                ).on("click") { event ->
                    event.preventDefault()
                    window.location.href = bo.url()
                }
            }
        }
    }

}