/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.sidebar

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.core.browser.application.application
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.localizedStrings

/**
 * A sidebar item with an optional custom zkElement instead of icon.
 *
 * @property  text     The text to display.
 * @property component  A ZkElement to display on the left of the text (e.g.: ZkNotificationIcon)
 * @property  url      The URL the item points to. When specified, the group uses an "a" tag instead of
 *                     "div" and sets the "href" attribute to the value of this parameter.
 * @property  onClick  A function to call when the user clicks on the item text.
 */
class ZkSideBarItemWithNotification(
    text: String,
    val component: ZkElement? = null,
    url: String? = null,
    capitalize: Boolean = true,
    onClick: (() -> Unit)? = null
) : ZkSideBarItem(
    text = text,
    icon = null,
    url = url,
    capitalize = capitalize,
    onClick = onClick
) {

    constructor(
        target: ZkAppRouting.ZkTarget,
        component: ZkElement? = null,
        subPath: String? = null,
        text: String? = null,
        onClick: (() -> Unit)? = null
    ) : this(
        text = text ?: localizedStrings.getNormalized(target.viewName),
        component = component,
        url = application.routing.toLocalUrl(target, subPath),
        onClick = onClick
    )

    override fun onCreate() {
        + zkSideBarStyles.item

        if (url == null) {
            textElement = document.createElement("div") as HTMLElement
        } else {
            textElement = document.createElement("a") as HTMLElement
            url.let { (textElement as HTMLAnchorElement).href = it }
        }

        textElement.classList += zkSideBarStyles.itemText
        textElement.innerText = if (capitalize) text.capitalize() else text

        component?.let {
            + it
        }

        + textElement

        on("click", ::onClick)
        on("mousedown", ::onMouseDown)

    }

}