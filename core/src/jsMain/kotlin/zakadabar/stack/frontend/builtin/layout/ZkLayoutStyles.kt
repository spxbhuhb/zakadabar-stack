/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkLayoutStyles : ZkCssStyleSheet() {

    val defaultLayoutSmall by cssClass {
        display = "flex"
        flexDirection = "column"
        height = "100vh"
        width = "100vw"
        overflow = "hidden"
    }

    val defaultLayoutLarge by cssClass {
        display = "grid"
        gridTemplateColumns = "max-content 1fr"
        gridTemplateRows = "${theme.layout.titleBarHeight}px 1fr"
        height = "100vh"
        width = "100vw"
        overflow = "hidden"
    }

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

    val defaultBackground by cssClass {
        backgroundColor = theme.layout.defaultBackground
    }

    val grid by cssClass {
        display = "grid"
        borderCollapse = "collapse"
        border = 0
    }

    val layout by cssClass {
        position = "absolute"
        top = 0
        left = 0
        width = "100vw"
        height = "100vh"
        overflow = "hidden"
    }

    val p1 by cssClass {
        padding = 8
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
        backgroundColor = theme.layout.sliderColor
        cursor = "row-resize"
        on(":hover") {
            backgroundColor = theme.layout.sliderHoverColor
        }
    }

    val verticalSlider by cssClass {
        boxSizing = "border-box"
        height = "100%"
        width = 3
        minWidth = 3
        backgroundColor = theme.layout.sliderColor
        cursor = "col-resize"
        on(":hover") {
            backgroundColor = theme.layout.sliderHoverColor
        }
    }

    val page by cssClass {
        flexGrow = 1
        backgroundColor = theme.layout.defaultBackground
    }

    init {
        attach()
    }
}