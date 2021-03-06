/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.dock

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkDockStyles : CssStyleSheet<ZkDockStyles>(Application.theme) {

    val dock by cssClass {
        position = "fixed"
        right = 0
        bottom = 0
        display = "flex"
        flexDirection = "row"
        width = "100%"
        zIndex = 1000
        justifyContent = "flex-end"
    }

    val dockItem by cssClass {
        backgroundColor = theme.lightGray
        display = "flex"
        flexDirection = "column"
    }

    init {
        attach()
    }
}