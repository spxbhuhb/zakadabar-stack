/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

import zakadabar.stack.frontend.elements.ZkElement

open class ZkMenu() : ZkElement() {

    constructor(builder: ZkMenu.() -> Unit) : this() {
        className = ZkMenuStyles.menu
        builder()
    }

    open fun title(text: String, onClick: (() -> Unit)? = null) =
        ZkMenuTitle(text, onClick)

    open fun item(text: String, onClick: (() -> Unit)? = null) =
        ZkMenuItem(text, onClick)

    open fun group(text: String, builder: ZkElement.() -> Unit) =
        ZkMenuGroup(text, builder)
}
