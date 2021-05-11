/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkIconButton
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.plusAssign

/**
 * The top-left part of the default layout, a button to close the menu and the title of the application.
 */
open class ZkAppHandle(
    private val appName: ZkElement,
    private val onIconClick: (() -> Unit)? = null,
    private val onTextClick: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {
        classList += zkTitleBarStyles.appHandleContainer

        + ZkIconButton(ZkIcons.notes, cssClass = zkTitleBarStyles.appHandleButton, onClick = onIconClick) marginRight 10
        + appName build {
            on("click") { onTextClick?.invoke() }
        }

        on("mousedown", ::onMouseDown)
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}