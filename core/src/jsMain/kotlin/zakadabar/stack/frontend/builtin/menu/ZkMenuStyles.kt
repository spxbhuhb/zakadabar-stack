/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.menu

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkMenuStyles : CssStyleSheet<ZkMenuStyles>(Application.theme) {

    val menu by cssClass {
        height = "100%"
        backgroundColor = theme.menu.background
        color = theme.menu.text
        minWidth = 200
        paddingLeft = 10
        paddingRight = 10
    }

    val title by cssClass {
        fontWeight = 500
        fontSize = "120%"
        paddingTop = 12
        paddingBottom = 4
        borderBottom = "1px solid white"
        marginBottom = 4
    }

    val item by cssClass {
        cursor = "pointer"
        padding = 10
        on(":hover") {
            backgroundColor = theme.menu.hoverBackground
            color = theme.menu.hoverText
            borderRadius = 4
        }
    }

    val groupTitle by cssClass {
        cursor = "pointer"
        padding = 10
        on(":hover") {
            backgroundColor = theme.menu.hoverBackground
            color = theme.menu.hoverText
            borderRadius = 4
        }
    }

    val groupContent by cssClass {
        paddingLeft = 20
    }

    init {
        attach()
    }
}