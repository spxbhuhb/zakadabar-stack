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
import zakadabar.core.resource.localizedStrings

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

    open lateinit var styles: SideBarStyleSpec

    open lateinit var openIcon: ZkElement
    open lateinit var closeIcon: ZkElement
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

        styles = sideBar?.styles ?: zkSideBarStyles

        val arrowSize = sideBar?.arrowSize ?: 18

        if (sideBar?.arrowAfter != true) {
            openIcon = ZkIcon(styles.groupOpenIcon, arrowSize)
            closeIcon = ZkIcon(styles.groupCloseIcon, arrowSize)
        } else {
            openIcon = ZkIcon(styles.afterGroupOpenIcon, arrowSize)
            closeIcon = ZkIcon(styles.afterGroupCloseIcon, arrowSize)
        }

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

        + div(styles.groupTitle) {

            if (sideBar?.arrowAfter != true) {
                arrow()
            } else {
                + div { + styles.groupArrow } // so the indentation is OK
            }

            icon?.let {
                + ZkIcon(icon) css styles.icon
            }

            + textElement css zkLayoutStyles.grow

            if (sideBar?.arrowAfter == true) arrow()

            on("click", ::onNavigate)
        }

        + zke(styles.groupContent) {
            if (! section) hide()
            builder()
        }
    }

    open fun arrow() {
        + div(styles.groupArrow) {
            + openIcon
            + closeIcon.hide()
            on("click", ::onHandleGroupClick)
        }
    }

    open fun buildSection() {
        open = true
        + column {
            + div(styles.sectionTitle) {
                icon?.let {
                    + ZkIcon(icon).on("click", ::onNavigate) css styles.icon
                }
                + textElement.on("click", ::onNavigate) css zkLayoutStyles.grow
            }
            + zke(styles.sectionContent) {
                builder()
            }
        }
    }

    open fun onHandleGroupClick(event: Event) {
        event as MouseEvent
        event.stopPropagation()

        open = if (open) {
            get<ZkElement>(styles.groupContent).hide()
            closeIcon.hide()
            openIcon.show()
            false
        } else {
            get<ZkElement>(styles.groupContent).show()
            openIcon.hide()
            closeIcon.show()
            true
        }
    }

    open fun onNavigate(event: Event) {
        if (! section) {
            if (
                (! open && sideBar?.arrowClose == false) ||
                (open && sideBar?.arrowOpen == false)
            ) {
                onHandleGroupClick(event)
            }
        }

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