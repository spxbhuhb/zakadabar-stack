/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import kotlinx.browser.document
import zakadabar.stack.frontend.builtin.ZkElement
import zakadabar.stack.frontend.builtin.button.ZkButton
import zakadabar.stack.frontend.resources.ZkFlavour
import zakadabar.stack.frontend.resources.ZkIcons
import zakadabar.stack.frontend.util.plusAssign
import zakadabar.stack.util.PublicApi

@PublicApi
open class ZkAppTitleBar(
    var onToggleSideBar: () -> Unit = { }
) : ZkElement() {

    open val handleContainer = ZkElement()
    open val titleContainer = ZkElement()
    open val contextElements = ZkElement()
    open val globalElements = ZkElement()

    open var title: ZkAppTitle? = null
        set(value) {
            titleContainer -= field // this is necessary so clears onTitleChange won't destroy the title
            field = value
            onTitleChange(value)
        }

    override fun onCreate() {
        classList += zkTitleBarStyles.appTitleBar

        + handleContainer css zkTitleBarStyles.sidebarHandle build {
            hide()
            + ZkButton(ZkIcons.notes, flavour = ZkFlavour.Custom, onClick = ::onHandleClick)
        }

        + titleContainer css zkTitleBarStyles.titleContainer marginRight 10

        + div { style { flexGrow = "1" } }

        + contextElements css zkTitleBarStyles.contextElementContainer marginRight 10

        + globalElements css zkTitleBarStyles.globalElementContainer marginRight 10
    }

    open fun onTitleChange(value: ZkAppTitle?) {
        titleContainer.childElements.forEach { titleContainer -= it }
        contextElements.childElements.forEach { titleContainer -= it }

        if (value == null) return

        document.title = value.text
        titleContainer += value

        value.contextElements.forEach {
            contextElements += it marginRight 10
        }
    }

    open fun onHandleClick() {
        onToggleSideBar()
    }

}
