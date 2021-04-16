/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.builtin.ZkElement

open class ZkSideBar : ZkElement() {

    override fun onCreate() {
        className = ZkSideBarStyles.sidebar
    }

    @Suppress("DeprecatedCallableAddReplaceWith") // replacement is ZkAppHandle but it is set at a different place
    @Deprecated("use ZkAppHandle and ZkDefaultLayout instead")
    open fun title(text: String, onIconClick: (() -> Unit)? = null, onTextClick: (() -> Unit)? = null) =
        ZkSideBarTitle(text, onIconClick, onTextClick)

    open fun item(text: String, capitalize: Boolean = true, onClick: (() -> Unit)? = null) =
        ZkSideBarItem(text, capitalize, onClick)

    open fun group(text: String, builder: ZkElement.() -> Unit) =
        ZkSideBarGroup(text, builder)
}
