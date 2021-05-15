/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.application.application
import zakadabar.stack.frontend.application.stringStore
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.icon.ZkIcon
import zakadabar.stack.frontend.builtin.layout.zkLayoutStyles
import zakadabar.stack.frontend.resources.ZkIcons

/**
 * A group of sidebar entries. Supports open and close.
 *
 * @property  text     The text to display.
 * @property  url      The URL the group points to. When specified, the group uses an "a" tag instead of
 *                     "div" and sets the "href" attribute to the value of this parameter.
 * @property  onClick  A function to call when the user clicks on the group title. The parameter shows
 *                     if the bar is open or close. True means that the user opened a bar just now,
 *                     false means that the user closed it.
 * @property  builder  The builder function that builds the content of the group.
 */
open class ZkSideBarGroup(
    open val text: String,
    open val url: String? = null,
    open var onClick: ((Boolean) -> Unit)? = null,
    open val builder: ZkElement.() -> Unit
) : ZkElement() {

    open var open = false
    open val openIcon = ZkIcon(ZkIcons.arrowRight, 18)
    open val closeIcon = ZkIcon(ZkIcons.arrowDropDown, 18)
    open lateinit var textElement : ZkElement

    constructor(
        target : ZkAppRouting.ZkTarget,
        subPath : String? = null,
        text : String? = null,
        onClick: ((Boolean) -> Unit)? = null,
        builder: ZkElement.() -> Unit
    )  : this(
        text = text ?: stringStore.getNormalized(target.viewName),
        url = application.routing.toLocalUrl(target, subPath),
        onClick = onClick,
        builder = builder
    )

    open val localNav
        get() = url == null || (url?.startsWith("https://") != true && url?.startsWith("http://") != true)

    override fun onCreate() {

        if (url == null) {
            textElement = ZkElement(document.createElement("div") as HTMLElement)
        } else {
            textElement = ZkElement(document.createElement("a") as HTMLElement)
            url?.let { (textElement.element as HTMLAnchorElement).href = it }
        }

        textElement.innerText = text

        + column {
            + div(zkSideBarStyles.groupTitle) {
                + div {
                    + openIcon
                    + closeIcon.hide()
                    on("click", ::onHandleClick)
                }
                + textElement.on("click", ::onNavigate) css zkLayoutStyles.grow
            }
            + zke(zkSideBarStyles.groupContent) {
                hide()
                builder()
            }
        }
    }

    open fun onHandleClick(event : Event) {
        event as MouseEvent

        if (open) {
            get<ZkElement>(zkSideBarStyles.groupContent).hide()
            closeIcon.hide()
            openIcon.show()
            open = false
        } else {
            get<ZkElement>(zkSideBarStyles.groupContent).show()
            openIcon.hide()
            closeIcon.show()
            open = true
        }
    }

    open fun onNavigate(event : Event) {
        onHandleClick(event)

        if (localNav) {
            event.preventDefault()
            if (onClick != null) {
                onClick?.invoke(open)
            } else {
                url?.let { application.changeNavState(it) }
            }
        }
    }


}