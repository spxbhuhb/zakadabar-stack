/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import org.w3c.dom.events.Event
import zakadabar.stack.frontend.builtin.icon.IconSource
import zakadabar.stack.frontend.elements.ZkElement
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
    private val icon: IconSource,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    override fun init(): ZkElement {

        className = ZkButtonStyles.iconButton

        innerHTML = icon.svg(24)

        on("click", onClick)
        on("mousedown", ::onMouseDown)

        return this
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}