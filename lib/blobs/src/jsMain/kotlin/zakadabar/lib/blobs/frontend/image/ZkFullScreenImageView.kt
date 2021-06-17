/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.lib.blobs.frontend.image

import kotlinx.browser.document
import org.w3c.dom.events.KeyboardEvent
import zakadabar.lib.blobs.frontend.blobStyles
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementState
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.resources.ZkIcons

class ZkFullScreenImageView(
    val url: String,
    val onDeleteImage: (preview: ZkFullScreenImageView) -> Unit
) : ZkElement() {

    override fun onCreate() {

        + blobStyles.outerView

        element.tabIndex = 0

        + image(url, blobStyles.image)

        + ZkButton(
            ZkIcons.close,
            buttonSize = 48,
            iconSize = 32,
            round = true
        ) {
            hide()
        } css blobStyles.closeButton

        + ZkButton(
            ZkIcons.deleteForever,
            buttonSize = 48,
            iconSize = 32,
            round = true
        ) {
            onDeleteImage(this)
        } css blobStyles.deleteButton

        on("keydown") { event ->
            event as KeyboardEvent
            when (event.key) {
                "Escape" -> {
                    hide()
                }
            }
        }
    }

    override fun show(): ZkElement {
        document.body?.appendChild(element)
        if (lifeCycleState != ZkElementState.Created) {
            onCreate()
        }
        focus()
        return this
    }

    override fun hide(): ZkElement {
        document.body?.removeChild(element)
        return this
    }
}