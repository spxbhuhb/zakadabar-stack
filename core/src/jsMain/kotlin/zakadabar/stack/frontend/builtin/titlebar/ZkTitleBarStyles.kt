/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkTitleBarStyles : CssStyleSheet<ZkTitleBarStyles>(Application.theme) {

    val titleBar by cssClass {
        minHeight = 44 // linked to ZkMenuStyles.title.height
        backgroundColor = "rgb(245,245,245)"
        borderBottom = "0.5px solid #ccc"
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        paddingLeft = 20
        fontSize = 16
    }

    init {
        attach()
    }
}