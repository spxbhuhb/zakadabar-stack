/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.button

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkButtonStyles : CssStyleSheet<ZkButtonStyles>(Application.theme) {

    val button by cssClass {
        fontFamily = theme.fontFamily
        fontSize = 14
        color = theme.button.text
        textTransform = "uppercase"
        cursor = "pointer"
        backgroundColor = theme.button.background
        paddingTop = 6
        paddingBottom = 6
        paddingLeft = 10
        paddingRight = 10
        borderWidth = 1
        borderRadius = 4
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

        borderRadius = 4
        cursor = "pointer"

        background = theme.button.background
        fill = theme.button.text

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
        fill = theme.button.text

    }

    init {
        attach()
    }

}