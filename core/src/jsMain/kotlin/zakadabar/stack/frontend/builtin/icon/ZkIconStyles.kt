/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.icon

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkIconStyles : CssStyleSheet<ZkIconStyles>(Application.theme) {

    val icon by cssClass {
        boxSizing = "border-box"

        // these assume the icon is 24x24 pixels
        width = 24
        height = 24

        background = theme.icon.background
        fill = theme.icon.foreground

    }

    init {
        attach()
    }

}