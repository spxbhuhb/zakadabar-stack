/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkButtonStyles : ZkCssStyleSheet<ZkTheme>() {

    val button by cssClass {
        width = "max-content"
        fontSize = 14
        color = theme.button.foreground
        cursor = "pointer"
        backgroundColor = theme.button.background
        paddingTop = 6
        paddingBottom = 6
        paddingLeft = 10
        paddingRight = 10
        borderWidth = 1
        borderRadius = 2
        on(":hover") {
            backgroundColor = theme.button.hoverBackground
            color = theme.button.hoverText
        }
    }

    val iconButton by cssClass {
        boxSizing = "border-box"

        // these assume the icon is 24x24 pixels
        height = 22
        width = 22

        display = "flex"
        justifyContent = "center"
        alignItems = "center"

        borderRadius = 2
        cursor = "pointer"

        background = theme.button.background
        fill = theme.button.iconFill

    }

    val roundButton by cssClass {
        boxSizing = "border-box"

        // these assume the icon is 20x20 pixels
        height = 22
        width = 22

        display = "flex"
        justifyContent = "center"
        alignItems = "center"

        borderRadius = 11
        cursor = "pointer"

        background = theme.button.background
        fill = theme.button.foreground

    }

    val transparent by cssClass {
        background = "transparent"
    }

    init {
        attach()
    }

}