/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.application.ZkAppRouting
import zakadabar.stack.frontend.builtin.ZkElement

open class ZkSideBar : ZkElement() {

    override fun onCreate() {
        + zkSideBarStyles.sidebar
    }

    open fun item(text: String, capitalize: Boolean = true, onClick: (() -> Unit)? = null) =
        ZkSideBarItem(text, null, capitalize, onClick)

    open fun item(target : ZkAppRouting.ZkTarget, subPath : String? = null, text : String? = null) = ZkSideBarItem(target, subPath, text)

    open fun group(text: String, onClick: ((Boolean) -> Unit)? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, onClick = onClick, builder = builder)

    open fun group(target : ZkAppRouting.ZkTarget, text : String? = null, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(target, text = text, builder = builder)

}
