/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkLayoutStyles by cssStyleSheet(ZkLayoutStyles())

class ZkLayoutStyles : ZkCssStyleSheet<ZkTheme>() {

    // -------------------------------------------------------------------------
    // General
    // -------------------------------------------------------------------------

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

    val border by cssClass {
        border = theme.border
    }

    // -------------------------------------------------------------------------
    // ZkAppLayout
    // -------------------------------------------------------------------------

    val layout by cssClass {
        position = "absolute"
        top = 0
        left = 0
        width = "100%"
        height = "100%"
        overflow = "hidden"
    }

    // -------------------------------------------------------------------------
    // ZkDefaultLayout
    // -------------------------------------------------------------------------

    val defaultLayoutSmall by cssClass {
        display = "flex"
        flexDirection = "column"
        height = "100%"
        width = "100%"
        overflow = "hidden"
    }

    val defaultLayoutLarge by cssClass {
        display = "grid"
        gridTemplateColumns = "max-content 1fr"
        gridTemplateRows = "${theme.titleBar.height} 1fr"
        height = "100%"
        width = "100%"
        overflow = "hidden"
    }

    val sideBarContainer by cssClass {
        maxHeight = "100%"
        overflowY = "auto"
    }

    val popupSideBarContainer by cssClass {
        background = theme.layout.defaultBackground
        zIndex = 100
        position = "absolute"
        width = "100%"
    }

    val contentContainer by cssClass {
        height = "100%"
        maxHeight = "100%"
        overflowY = "hidden"
    }

    // -------------------------------------------------------------------------
    // Slider
    // -------------------------------------------------------------------------

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

    init {
        attach()
    }
}