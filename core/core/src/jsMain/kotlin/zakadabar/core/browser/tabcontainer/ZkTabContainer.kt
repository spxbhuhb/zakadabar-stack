/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.tabcontainer

import zakadabar.core.browser.ZkElement
import zakadabar.core.browser.util.plusAssign
import zakadabar.core.resource.localizedStrings
import zakadabar.core.util.PublicApi

open class ZkTabContainer(
    open val styles: TabContainerStyleSpec = zkTabContainerStyles,
    val builder: (ZkTabContainer.() -> Unit)? = null
) : ZkElement() {

    open val tabLabels = ZkElement()
    open val tabContents = ZkElement()

    open val items = mutableListOf<TabItem>()
    open lateinit var activeItem: TabItem

    override fun onCreate() {
        classList += styles.container
        + tabLabels css styles.labels
        + tabContents css styles.contentContainer

        builder?.invoke(this)
    }

    @PublicApi
    open fun tab(title: String, scroll: Boolean = true, border: Boolean = true, pad: Boolean = true, builder: ZkElement.() -> Unit) : TabItem {
        return if (scroll || border || pad) {
            TabItem(this, zke {
                if (scroll) + styles.scrolledContent
                if (border) + styles.border
                if (pad) + styles.padding
                builder()
            }, title)
        } else {
            TabItem(this, zke(build = builder), title)
        }
    }

    @PublicApi
    open fun tab(element: ZkElement, title : String? = null, scroll: Boolean = true, border: Boolean = true, pad: Boolean = true) : TabItem {
        return if (scroll || border || pad) {
            TabItem(this, element build {
                if (scroll) + styles.scrolledContent
                if (border) + styles.border
                if (pad) + styles.padding
            }, title ?: localizedStrings.getNormalized(element))
        } else {
            TabItem(this, element, title)
        }
    }

    open operator fun TabItem.unaryPlus() {
        items += this
        tabLabels += this.label

        if (items.size == 1) {
            activeItem = this
            this.label.active = true
            this@ZkTabContainer.tabContents += this.content
        }
    }

    open fun switchTo(item: TabItem) {
        if (activeItem == item) return

        activeItem.label.active = false
        tabContents -= activeItem.content

        activeItem = item

        activeItem.label.active = true
        tabContents += activeItem.content
    }

}
