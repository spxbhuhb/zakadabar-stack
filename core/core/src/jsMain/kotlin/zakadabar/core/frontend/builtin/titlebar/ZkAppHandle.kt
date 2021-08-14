/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.titlebar

import org.w3c.dom.events.Event
import zakadabar.core.frontend.application.ZkAppRouting
import zakadabar.core.frontend.application.application
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.frontend.builtin.button.ZkButton
import zakadabar.core.frontend.resources.ZkFlavour
import zakadabar.core.frontend.resources.ZkIcons
import zakadabar.core.frontend.util.plusAssign

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