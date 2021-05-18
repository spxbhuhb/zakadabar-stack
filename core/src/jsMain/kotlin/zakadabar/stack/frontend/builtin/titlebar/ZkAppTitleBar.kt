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

/**
 * @param  onToggleSideBar  Function to execute then the user switches on the side bar.
 * @param  fixTitle         A fix application title, when not null title changes have no effect and this
 *                          element is displayed constantly.
 */
@PublicApi
open class ZkAppTitleBar(
    var onToggleSideBar: () -> Unit = { },
    var fixTitle : ZkAppTitle? = null
) : ZkElement() {

    open val handleContainer = ZkElement()
    open val titleContainer = ZkElement()
    open val contextElements = ZkElement()
    open val globalElements = ZkElement()

    open var title: ZkAppTitle? = fixTitle
        set(value) {
            if (fixTitle != null) return
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

        if (fixTitle != null) {
            onTitleChange(fixTitle)
        }

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
