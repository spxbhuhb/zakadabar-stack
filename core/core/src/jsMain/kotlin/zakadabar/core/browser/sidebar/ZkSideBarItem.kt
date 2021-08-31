/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.sidebar

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.core.browser.application.application
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.ZkIconSource
import zakadabar.core.resource.localizedStrings
import zakadabar.core.text.capitalized

/**
 * A sidebar item.
 *
 * @property  text     The text to display.
 * @property  url      The URL the item points to. When specified, the group uses an "a" tag instead of
 *                     "div" and sets the "href" attribute to the value of this parameter.
 * @property  onClick  A function to call when the user clicks on the item text.
 */
open class ZkSideBarItem(
    val text: String,
    val icon: ZkIconSource? = null,
    val url: String? = null,
    val capitalize: Boolean = true,
    val onClick: (() -> Unit)? = null
) : ZkElement() {

    open lateinit var textElement: HTMLElement

    constructor(
        target: ZkAppRouting.ZkTarget,
        icon: ZkIconSource? = null,
        subPath: String? = null,
        text: String? = null,
        onClick: (() -> Unit)? = null
    ) : this(
        text = text ?: localizedStrings.getNormalized(target.viewName),
        icon = icon,
        url = application.routing.toLocalUrl(target, subPath),
        onClick = onClick
    )

    open val localNav
        get() = url == null || (! url.startsWith("https://") && ! url.startsWith("http://"))

    override fun onCreate() {
        + zkSideBarStyles.item

        if (url == null) {
            textElement = document.createElement("div") as HTMLElement
        } else {
            textElement = document.createElement("a") as HTMLElement
            url.let { (textElement as HTMLAnchorElement).href = it }
        }

        textElement.classList += zkSideBarStyles.itemText
        textElement.innerText = if (capitalize) text.capitalized() else text

        icon?.let {
            + ZkIcon(icon, size = zkSideBarStyles.iconSize) css zkSideBarStyles.icon
        }

        + textElement

        on("click", ::onClick)
        on("mousedown", ::onMouseDown)

    }

    open fun onClick(event: Event) {
        if (localNav) {
            event.preventDefault()
            if (onClick != null) {
                onClick.invoke()
            } else {
                url?.let { application.changeNavState(it) }
            }
        }
    }

    open fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}