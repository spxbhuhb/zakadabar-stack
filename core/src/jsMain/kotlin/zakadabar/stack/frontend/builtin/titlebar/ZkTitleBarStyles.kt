/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkTitleBarStyles : ZkCssStyleSheet<ZkTitleBarStyles>(ZkApplication.theme) {

    val titleBar by cssClass {
        boxSizing = "border-box"
        fontWeight = 400
        width = "100%"
        minHeight = theme.layout.titleBarHeight
        maxHeight = theme.layout.titleBarHeight
        backgroundColor = ZkColors.Gray.c100
        borderBottom = "0.5px solid #ccc"
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        paddingLeft = theme.layout.paddingStep * 2
        fontSize = 16
    }

    val titleContainer by cssClass {
        alignItems = "center"
    }

    val contextElementContainer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
    }

    val globalElementContainer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
    }

    init {
        attach()
    }
}