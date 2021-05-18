/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.builtin.ZkElement

open class ZkSideBar : ZkElement() {

    open val minimizedSections = ZkElement()

    override fun onCreate() {
        + minimizedSections css zkSideBarStyles.minimizedSectionContainer
        + zkSideBarStyles.sidebar
    }

    open fun item(text: String, capitalize: Boolean = true, onClick: (() -> Unit)? = null) =
        ZkSideBarItem(text, null, capitalize, onClick)

    open fun item(target : ZkAppRouting.ZkTarget, subPath : String? = null, text : String? = null) =
        ZkSideBarItem(target, subPath, text)

    open fun section(text: String, onClick: ((Boolean) -> Unit)? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, section = true, onClick = onClick, builder = builder, sideBar = this)

    open fun group(text: String, section : Boolean = false, onClick: ((Boolean) -> Unit)? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, section = section, onClick = onClick, builder = builder, sideBar = this)

    open fun group(target : ZkAppRouting.ZkTarget, text : String? = null, section : Boolean = false, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target, text = text, section = section, builder = builder, sideBar = this)

}
