/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkTitleBarStyles : ZkCssStyleSheet<ZkTitleBarStyles>(ZkApplication.theme) {

    val titleBar by cssClass {
        fontWeight = 400
        minHeight = 44 // linked to ZkMenuStyles.title.height
        backgroundColor = ZkColors.Gray.c100
        borderBottom = "0.5px solid #ccc"
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        paddingLeft = theme.layout.paddingStep * 2
        fontSize = 16
    }

    init {
        attach()
    }
}