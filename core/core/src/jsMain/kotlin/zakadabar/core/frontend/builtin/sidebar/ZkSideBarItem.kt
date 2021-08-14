/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.frontend.builtin.sidebar

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import zakadabar.core.frontend.application.ZkAppRouting
import zakadabar.core.frontend.application.application
import zakadabar.core.frontend.builtin.ZkElement
import zakadabar.core.resources.localizedStrings
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
    private val text: String,
    open val url: String? = null,
    private val capitalize: Boolean = true,
    private val onClick: (() -> Unit)? = null
) : ZkElement() {

    open lateinit var textElement: HTMLElement

    constructor(
        target: ZkAppRouting.ZkTarget,
        subPath: String? = null,
        text: String? = null,
        onClick: (() -> Unit)? = null
    ) : this(
        text = text ?: localizedStrings.getNormalized(target.viewName),
        url = application.routing.toLocalUrl(target, subPath),
        onClick = onClick
    )

    open val localNav
        get() = url == null || (url?.startsWith("https://") != true && url?.startsWith("http://") != true)

    override fun onCreate() {
        + zkSideBarStyles.item

        if (url == null) {
            textElement = document.createElement("div") as HTMLElement
        } else {
            textElement = document.createElement("a") as HTMLElement
            url?.let { (textElement as HTMLAnchorElement).href = it }
        }

        textElement.innerText = if (capitalize) text.capitalized() else text

        + textElement

        on("click", ::onClick)
        on("mousedown", ::onMouseDown)

    }

    private fun onClick(event: Event) {
        if (localNav) {
            event.preventDefault()
            if (onClick != null) {
                onClick.invoke()
            } else {
                url?.let { application.changeNavState(it) }
            }
        }
    }

    private fun onMouseDown(event: Event) {
        event.preventDefault() // to prevent focus change
    }

}