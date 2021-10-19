/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.icon

import org.w3c.dom.events.Event
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkIconSource

/**
 * A simple icon without any extra.
 *
 * @property  iconSvg   SVG source text of the icon.
 * @property  iconSize  Desired size of the icon in pixels.
 * @property  cssClass  Additional CSS class to add to the icon.
 * @property  onClick   Function to call when the icon is clicked.
 */
open class ZkIcon(
    open val iconSvg: String,
    open val iconSize: Int,
    open val cssClass: String? = null,
    open val onClick: (() -> Unit)? = null
) : ZkElement() {

    constructor(source: ZkIconSource, size: Int = 18, onClick: (() -> Unit)? = null) : this(source.svg(size), size, onClick = onClick)

    override fun onCreate() {
        classList += cssClass

        when (iconSize) {
            18 -> classList += zkIconStyles.icon18
            20 -> classList += zkIconStyles.icon20
            22 -> classList += zkIconStyles.icon22
            24 -> classList += zkIconStyles.icon24
            else -> zkIconStyles.setInlineStyles(element, iconSize)
        }

        element.innerHTML = iconSvg

        if (onClick != null) {
            on("click") { onClick?.invoke() }
            on("mousedown", ::onMouseDown)
        }
    }

    protected fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}