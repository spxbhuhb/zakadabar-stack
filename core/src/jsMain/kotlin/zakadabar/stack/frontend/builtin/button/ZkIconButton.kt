/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.icon.ZkIconSource
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.util.PublicApi

/**
 * A simple clickable button displaying an icon.
 *
 * @property  icon     The icon to display.
 * @property  onClick  The function to execute when the button is clicked.
 *
 * @since  2021.1.18
 */
@PublicApi
class ZkIconButton(
    private val icon: ZkIconSource,
    private val iconSize: Int = 18,
    private val buttonSize: Int = 22,
    private val cssClass: String? = null,
    private val fill: String? = null,
    private val round: Boolean = false,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {
        className = if (round) ZkButtonStyles.roundButton else ZkButtonStyles.iconButton
        if (cssClass != null) classList += cssClass

        element.style.width = "${buttonSize}px"
        element.style.height = "${buttonSize}px"

        if (round) {
            element.style.borderRadius = "${buttonSize / 2}px"
        }

        if (fill != null) {
            element.style.setPropertyValue("fill", fill) // this is not in the wrapper for some reason
        }

        innerHTML = icon.svg(iconSize)

        on("click") { _ -> onClick?.invoke() }
        on("mousedown", ::onMouseDown)
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}