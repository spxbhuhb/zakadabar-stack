/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.sidebar

import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.application.ZkAppRouting
import zakadabar.core.browser.application.application
import zakadabar.core.browser.icon.ZkIcon
import zakadabar.core.browser.layout.zkLayoutStyles
import zakadabar.core.resource.ZkIconSource
import zakadabar.core.resource.ZkIcons
import zakadabar.core.resource.localizedStrings
import zakadabar.core.util.after

/**
 * A group of sidebar entries. Supports open and close.
 *
 * @property  text     The text to display.
 * @property  icon     An optional icon to show in front of the text.
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
    val text: String,
    val icon: ZkIconSource? = null,
    val section: Boolean = false,
    val url: String? = null,
    var onClick: ((Boolean) -> Unit)? = null,
    open val sideBar: ZkSideBar? = null,
    val builder: ZkElement.() -> Unit
) : ZkElement() {

    open var open = false

    open val openIcon = ZkIcon(ZkIcons.arrowRight, 18)
    open val closeIcon by after { if (section) ZkIcon(ZkIcons.horizontalRule, 18) else ZkIcon(ZkIcons.arrowDropDown, 18) }

    open lateinit var textElement: ZkElement

    constructor(
        target: ZkAppRouting.ZkTarget,
        icon: ZkIconSource? = null,
        subPath: String? = null,
        text: String? = null,
        section: Boolean = false,
        sideBar: ZkSideBar? = null,
        onClick: ((Boolean) -> Unit)? = null,
        builder: ZkElement.() -> Unit
    ) : this(
        text = text ?: localizedStrings.getNormalized(target.viewName),
        icon = icon,
        section = section,
        url = application.routing.toLocalUrl(target, subPath),
        onClick = onClick,
        sideBar = sideBar,
        builder = builder
    )

    open val localNav
        get() = url == null || (! url.startsWith("https://") && ! url.startsWith("http://"))

    override fun onCreate() {

        if (url == null) {
            textElement = ZkElement(document.createElement("div") as HTMLElement)
        } else {
            textElement = ZkElement(document.createElement("a") as HTMLElement)
            url.let { (textElement.element as HTMLAnchorElement).href = it }
        }

        textElement.innerText = text

        if (section) {
            buildSection()
        } else {
            buildGroup()
        }
    }

    open fun buildGroup() {
        + zkLayoutStyles.column

        + div(zkSideBarStyles.groupTitle) {
            + div(zkSideBarStyles.groupArrow) {
                + openIcon
                + closeIcon.hide()
                on("click", ::onHandleGroupClick)
            }
            icon?.let {
                + ZkIcon(icon) css zkSideBarStyles.icon
            }
            + textElement css zkLayoutStyles.grow
            on("click", ::onNavigate)
        }
        + zke(zkSideBarStyles.groupContent) {
            if (! section) hide()
            builder()
        }
    }

    open fun buildSection() {
        open = true
        + column {
            + div(zkSideBarStyles.sectionTitle) {
                icon?.let {
                    + ZkIcon(icon).on("click", ::onNavigate) css zkSideBarStyles.icon
                }
                + textElement.on("click", ::onNavigate) css zkLayoutStyles.grow
                + closeIcon css zkSideBarStyles.sectionCloseIcon
                closeIcon.on("click", ::onHandleSectionClick)
            }
            + zke(zkSideBarStyles.sectionContent) {
                builder()
            }
        }
    }

    open fun onHandleGroupClick(event: Event) {
        event as MouseEvent
        event.stopPropagation()

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
        if (open) {
            minimize()
        } else {
            restore()
        }
    }

    open fun minimize() {
        if (open) {
            this.hide()
            ZkSidebarMinimizedSection(this.text.substring(0, 1), this).run()
            open = false
        }
    }

    open fun restore() {
        if (! open) {
            this.show()
            open = true
        }
    }

    open fun onNavigate(event: Event) {
        if (! section) onHandleGroupClick(event)

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