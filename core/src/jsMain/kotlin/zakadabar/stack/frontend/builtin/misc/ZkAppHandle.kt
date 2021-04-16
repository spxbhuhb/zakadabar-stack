/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButtonStyles
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.resources.ZkIcons

/**
 * The top-left part of the default layout, a button to close the menu and the title of the application.
 */
open class ZkAppHandle(
    private val text: String,
    private val onIconClick: (() -> Unit)? = null,
    private val onTextClick: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {
        + ZkIconButton(ZkIcons.notes, cssClass = ZkButtonStyles.transparent, onClick = onIconClick) marginRight 10
        + div { + text }

        on("click") { onTextClick?.invoke() }
        on("mousedown", ::onMouseDown)
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}