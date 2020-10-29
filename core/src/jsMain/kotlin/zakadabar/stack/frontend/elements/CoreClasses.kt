/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.elements

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class CoreClasses(theme: Theme) : CssStyleSheet<CoreClasses>(theme) {

    companion object {
        val coreClasses = CoreClasses(FrontendContext.theme).attach()
    }

    val green by cssClass {
        color = "#398d39"
    }

    val orange by cssClass {
        color = "#ec9d1d"
    }

    val w100 by cssClass {
        width = "100%"
    }

    val h100 by cssClass {
        height = "100%"
    }

    val whMax100p by cssClass {
        maxWidth = "100%"
        maxHeight = "100%"
    }

    val mainContent by cssClass {
        width = "100%"
        display = "flex"
        flexDirection = "column"
        width = theme.contentWidth
        alignItems = "flex-start"
    }

    val contentColumn by cssClass {
        width = "100%"
        display = "flex"
        flexDirection = "column"
        alignItems = "center"
    }

    val hidden by cssClass {
        display = "none !important"
    }

    val w100Row by cssClass {
        width = "100%"
        display = "flex"
        flexDirection = "row"
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

    val avatar by cssClass {
        backgroundColor = theme.darkColor
        width = 28
        height = 28
        fontSize = 14
        color = theme.lightestColor
        textAlign = "center"
        lineHeight = 28
        borderRadius = "50%"
    }

    val title by cssClass {
        fontSize = "150%"
        color = "black"
    }

    val smallInfo by cssClass {
        fontSize = "70%"
        color = theme.darkGray
    }

    val dock by cssClass {
        position = "fixed"
        right = 0
        bottom = 0
        display = "flex"
        flexDirection = "row"
        width = "100%"
        zIndex = 1000
        justifyContent = "flex-end"
    }

    val dockItem by cssClass {
        backgroundColor = theme.lightGray
        display = "flex"
        flexDirection = "column"
    }

}