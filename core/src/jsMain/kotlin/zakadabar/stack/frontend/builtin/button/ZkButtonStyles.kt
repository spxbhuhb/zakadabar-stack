/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkButtonStyles : CssStyleSheet<ZkButtonStyles>(Application.theme) {

    val iconButton by cssClass {
        boxSizing = "border-box"

        // these assume the icon is 24x24 pixels
        padding = 3
        height = 30

        borderRadius = 4
        cursor = "pointer"

        background = theme.button.background
        fill = theme.button.text

    }

    init {
        attach()
    }

}