/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.browser.attachment

import kotlinx.browser.window
import zakadabar.core.browser.button.ZkButton
import zakadabar.core.resource.ZkFlavour
import zakadabar.core.resource.ZkIcons
import zakadabar.lib.blobs.browser.ZkBlobFieldEntry
import zakadabar.lib.blobs.browser.blobStyles
import zakadabar.lib.blobs.data.BlobBo
import zakadabar.lib.blobs.data.BlobCreateState
import zakadabar.lib.blobs.data.url
import zakadabar.lib.blobs.resource.blobStrings

open class ZkAttachmentEntry<BT : BlobBo<BT, *>>(
    bo: BT,
    createState: BlobCreateState? = null,
    progress: Long? = null,
    size: Int = 200,
    onDelete: suspend (preview: ZkBlobFieldEntry<BT>) -> Boolean = { false }
) : ZkBlobFieldEntry<BT>(
    bo, createState, progress, size, onDelete
) {

    override fun onCreate() {
        + blobStyles.attachmentEntry
        render()
    }

    override fun render() {
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
                    url = bo.url,
                    onClick = { }
                ).on("click") { event ->
                    event.preventDefault()
                    window.location.href = bo.url
                }
            }
        }
    }

}