/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.button

import org.w3c.dom.events.Event
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkIconSource
import zakadabar.core.resource.css.ZkCssStyleRule
import zakadabar.core.util.PublicApi

/**
 * A simple clickable button displaying an icon.
 *
 * @property  icon     The icon to display.
 * @property  onClick  The function to execute when the button is clicked.
 *
 * @since  2021.1.18
 */
@PublicApi
@Deprecated("EOL: 2021.8.1  -  use ZkButton(ZkIcons.notes, flavour = ZkFlavour.Custom, onClick = ::onHandleClick) instead")
open class ZkIconButton(
    val icon: ZkIconSource,
    val iconSize: Int = 18,
    val buttonSize: Int = 22,
    val cssClass: ZkCssStyleRule? = null,
    val fill: String? = null,
    val round: Boolean = false,
    val onClick: (() -> Unit)? = null
) : ZkElement() {

    override fun onCreate() {
        classList += zkButtonStyles.icon
        if (round) classList += zkButtonStyles.round
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

        on("click", ::onMouseClick)
        on("mousedown", ::onMouseDown)
    }

    open fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

    open fun onMouseClick(event: Event) {
        onClick?.invoke()
    }

}