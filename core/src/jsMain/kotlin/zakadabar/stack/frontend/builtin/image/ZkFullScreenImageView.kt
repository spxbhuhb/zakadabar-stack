/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.image

import kotlinx.browser.document
import org.w3c.dom.events.KeyboardEvent
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.ZkElementState
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.resources.ZkIcons

class ZkFullScreenImageView(
    val url: String,
    val onDeleteImage: (preview: ZkFullScreenImageView) -> Unit
) : ZkElement() {

    override fun onCreate() {
        className = zkImageStyles.outerView
        element.tabIndex = 0

        + image(url, zkImageStyles.image)

        + ZkIconButton(
            ZkIcons.close,
            buttonSize = 48,
            iconSize = 32,
            cssClass = zkImageStyles.closeButton,
            round = true
        ) {
            hide()
        }

        + ZkIconButton(
            ZkIcons.deleteForever,
            buttonSize = 48,
            iconSize = 32,
            cssClass = zkImageStyles.deleteButton,
            round = true
        ) {
            onDeleteImage(this)
        }

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