/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout.tabcontainer

import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.util.PublicApi

open class ZkTabContainer(
    private val builder: (ZkTabContainer.() -> Unit)? = null
) : ZkElement() {

    private val tabLabels = ZkElement()
    val tabContents = ZkElement()

    private val items = mutableListOf<TabItem>()
    private lateinit var activeItem: TabItem

    override fun onCreate() {
        classList += zkTabContainerStyles.container
        + tabLabels css zkTabContainerStyles.labels
        + tabContents css zkTabContainerStyles.contentContainer
        builder?.invoke(this)
    }

    @PublicApi
    open fun tab(title: String, builder: ZkElement.() -> Unit) = TabItem(this, ZkElement().build(builder), title)

    @PublicApi
    open fun tab(title: String, zke: ZkElement) = TabItem(this, zke, title)

    operator fun TabItem.unaryPlus() {
        items += this
        tabLabels += this.label

        if (items.size == 1) {
            activeItem = this
            this.label.active = true
            this@ZkTabContainer.tabContents += this.content
        }
    }

    fun switchTo(item: TabItem) {
        if (activeItem == item) return

        activeItem.label.active = false
        tabContents -= activeItem.content

        activeItem = item

        activeItem.label.active = true
        tabContents += activeItem.content
    }

}
