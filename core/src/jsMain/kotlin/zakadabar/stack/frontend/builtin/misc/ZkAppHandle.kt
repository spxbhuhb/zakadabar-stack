/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.misc

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButtonStyles
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.builtin.sidebar.ZkSideBarStyles
import zakadabar.stack.frontend.builtin.titlebar.ZkTitleBarStyles
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.plusAssign

/**
 * The top-left part of the default layout, a button to close the menu and the title of the application.
 */
open class ZkAppHandle(
    private val text: ZkElement,
    private val onIconClick: (() -> Unit)? = null,
    private val onTextClick: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {
        classList += ZkTitleBarStyles.title

        + ZkIconButton(ZkIcons.notes, cssClass = ZkTitleBarStyles.handleButton, onClick = onIconClick) marginRight 10
        + zke {
            + text
            on("click") { onTextClick?.invoke() }
        }

        on("mousedown", ::onMouseDown)
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}