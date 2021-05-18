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
import zakadabar.stack.frontend.util.after

/**
 * A group of sidebar entries. Supports open and close.
 *
 * @property  text     The text to display.
 * @property  section  When true this group is a section in the sidebar. This means that the styling
 *                     is different. Default is false.
 * @property  url      The URL the group points to. When specified, the group uses an "a" tag instead of
 *                     "div" and sets the "href" attribute to the value of this parameter.
 * @property  onClick  A function to call when the user clicks on the group title. The parameter shows
 *                     if the bar is open or close. True means that the user opened a bar just now,
 *                     false means that the user closed it.
 * @property  sideBar  The sidebar this group is part of.
 * @property  builder  The builder function that builds the content of the group.
 */
open class ZkSideBarGroup(
    open val text: String,
    open val section: Boolean = false,
    open val url: String? = null,
    open var onClick: ((Boolean) -> Unit)? = null,
    open val sideBar: ZkSideBar? = null,
    open val builder: ZkElement.() -> Unit
) : ZkElement() {

    open var open = false
    open val openIcon = ZkIcon(ZkIcons.arrowRight, 18)
    open val closeIcon by after { if (section) ZkIcon(ZkIcons.close, 18) else ZkIcon(ZkIcons.arrowDropDown, 18) }
    open lateinit var textElement: ZkElement

    constructor(
        target: ZkAppRouting.ZkTarget,
        subPath: String? = null,
        text: String? = null,
        section: Boolean = false,
        sideBar: ZkSideBar? = null,
        onClick: ((Boolean) -> Unit)? = null,
        builder: ZkElement.() -> Unit
    ) : this(
        text = text ?: stringStore.getNormalized(target.viewName),
        section = section,
        url = application.routing.toLocalUrl(target, subPath),
        onClick = onClick,
        sideBar = sideBar,
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

        if (section) {
            buildSection()
        } else {
            buildGroup()
        }
    }

    open fun buildGroup() {
        + column {
            + div(zkSideBarStyles.groupTitle) {
                + div {
                    + openIcon
                    + closeIcon.hide()
                    on("click", ::onHandleGroupClick)
                }
                + textElement.on("click", ::onNavigate) css zkLayoutStyles.grow
            }
            + zke(zkSideBarStyles.groupContent) {
                if (! section) hide()
                builder()
            }
        }
    }

    open fun buildSection() {
        open = true
        + column {
            + div(zkSideBarStyles.sectionTitle) {
                + textElement.on("click", ::onNavigate) css zkLayoutStyles.grow
                + closeIcon
                closeIcon.on("click", ::onHandleSectionClick)
            }
            + zke(zkSideBarStyles.sectionContent) {
                builder()
            }
        }
    }

    open fun onHandleGroupClick(event: Event) {
        event as MouseEvent

        open = if (open) {
            get<ZkElement>(zkSideBarStyles.groupContent).hide()
            closeIcon.hide()
            openIcon.show()
            false
        } else {
            get<ZkElement>(zkSideBarStyles.groupContent).show()
            openIcon.hide()
            closeIcon.show()
            true
        }
    }

    open fun onHandleSectionClick(event: Event) {
        open = if (open) {
            this.hide()
            ZkSidebarMinimizedSection(this.text.substring(0, 1), this).run()
            false
        } else {
            this.show()
            true
        }
    }

    open fun onNavigate(event: Event) {
        onHandleGroupClick(event)

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