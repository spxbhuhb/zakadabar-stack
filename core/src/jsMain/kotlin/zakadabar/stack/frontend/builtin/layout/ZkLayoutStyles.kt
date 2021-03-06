/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.util.CssStyleSheet

object ZkLayoutStyles : CssStyleSheet<ZkLayoutStyles>(Application.theme) {

    val w100 by cssClass {
        width = "100%"
    }

    val h100 by cssClass {
        height = "100%"
    }

    val hidden by cssClass {
        display = "none !important"
    }

    val row by cssClass {
        display = "flex"
        flexDirection = "row"
    }

    val column by cssClass {
        display = "flex"
        flexDirection = "column"
    }

    val grow by cssClass {
        flexGrow = 1
    }

    val grid by cssClass {
        display = "grid"
        borderCollapse = "collapse"
        border = 0
    }

    val layoutContent by cssClass {
        flexGrow = 1
        display = "flex"
        overflow = "hidden"
    }

    val horizontalSlider by cssClass {
        boxSizing = "border-box"
        width = "100%"
        height = 3
        minHeight = 3
        backgroundColor = theme.sliderColor
        cursor = "row-resize"
        on(":hover") {
            backgroundColor = theme.selectedColor
        }
    }

    val verticalSlider by cssClass {
        boxSizing = "border-box"
        height = "100%"
        width = 3
        minWidth = 3
        backgroundColor = theme.sliderColor
        cursor = "col-resize"
        on(":hover") {
            backgroundColor = theme.selectedColor
        }
    }

    init {
        attach()
    }
}