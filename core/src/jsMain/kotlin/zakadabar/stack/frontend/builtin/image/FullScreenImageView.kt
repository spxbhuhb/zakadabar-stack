/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.image

import kotlinx.browser.document
import org.w3c.dom.events.KeyboardEvent
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.elements.ZkElement
import zakadabar.stack.frontend.resources.Icons

class FullScreenImageView(
    val url: String,
    val onDelete: (preview: FullScreenImageView) -> Unit
) : ZkElement() {

    init {
        className = ZkImageStyles.outerView
        element.tabIndex = 0

        + image(url, ZkImageStyles.image)

        + ZkIconButton(
            Icons.close,
            buttonSize = 48,
            iconSize = 32,
            cssClass = ZkImageStyles.closeButton,
            round = true
        ) {
            hide()
        }

        + ZkIconButton(
            Icons.deleteForever,
            buttonSize = 48,
            iconSize = 32,
            cssClass = ZkImageStyles.deleteButton,
            round = true
        ) {
            onDelete(this)
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
        focus()
        return this
    }

    override fun hide(): ZkElement {
        document.body?.removeChild(element)
        return this
    }
}