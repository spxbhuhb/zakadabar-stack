/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.plusAssign

/**
 * The top-left part of the default layout, a button to close the menu and the title of the application.
 */
open class ZkAppHandle(
    open val appName: ZkElement,
    open val target: ZkAppRouting.ZkTarget? = null,
    open val onIconClick: (() -> Unit)? = null,
    open val onTextClick: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {
        classList += zkTitleBarStyles.appHandleContainer

        + div(zkTitleBarStyles.appHandleButton) {
            + ZkButton(ZkIcons.notes, flavour = ZkFlavour.Custom, onClick = onIconClick)
        }
        + appName.on("click") {
            if (onTextClick != null) {
                onTextClick?.invoke()
            } else {
                target?.let { application.changeNavState(it) }
            }
        }

        on("mousedown", ::onMouseDown)
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}