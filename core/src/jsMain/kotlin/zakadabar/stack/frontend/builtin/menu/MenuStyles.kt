/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object MenuStyles : CssStyleSheet<MenuStyles>(Application.theme) {

    val menu by cssClass {
        height = "100%"
        backgroundColor = theme.menu.background
        color = theme.menu.text
        minWidth = 200
        marginRight = 10
    }

    val item by cssClass {
        cursor = "pointer"
        padding = 10
        on(":hover") {
            backgroundColor = theme.darkGray
            color = theme.lightestColor
        }
    }

    val groupTitle by cssClass {
        cursor = "pointer"
        padding = 10
        on(":hover") {
            backgroundColor = theme.darkGray
            color = theme.lightestColor
        }
    }

    val groupContent by cssClass {
        paddingLeft = 20
    }

    init {
        attach()
    }
}