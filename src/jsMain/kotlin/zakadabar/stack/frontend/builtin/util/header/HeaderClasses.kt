/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.header

import zakadabar.stack.frontend.FrontendContext
import zakadabar.stack.frontend.util.CssStyleSheet
import zakadabar.stack.frontend.util.Theme

class HeaderClasses(theme: Theme) : CssStyleSheet<HeaderClasses>(theme) {

    companion object {
        val headerClasses = HeaderClasses(FrontendContext.theme).attach()
    }

    val header by cssClass {
        display = "flex"
        flexDirection = "row"
        boxSizing = "border-box"
        height = 26
        alignItems = "center"
        backgroundColor = theme.headerBackground
        // marginBottom= theme.margin
        overflow = "hidden"
    }

    val headerIcon by cssClass {
        boxSizing = "border-box"
        backgroundColor = theme.headerForeground
        padding = 6
        height = 26
        width = 26
        fill = theme.lightestColor
    }

    val headerIcon18 by cssClass {
        boxSizing = "border-box"
        backgroundColor = theme.headerForeground
        padding = "5px 4px 4px 4px"
        height = 26
        fill = theme.lightestColor
    }

    val headerIcon20 by cssClass {
        boxSizing = "border-box"
        backgroundColor = theme.headerForeground
        padding = "4px 3px 3px 3px"
        height = 26
        fill = theme.lightestColor
    }

    val innerIcon by cssClass {
        boxSizing = "border-box"
        padding = "6px 0px 6px 6px"
        height = 26
        fill = theme.darkestColor
        cursor = "pointer"
    }

    val text by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = theme.headerForeground
        fontFamily = "'Open Sans', sans-serif"
        fontWeight = 600
        fontSize = 11
        height = 21
        paddingLeft = 8
        textTransform = "uppercase"
    }

    val extensions by cssClass {
        flexGrow = 1
        display = "flex"
        flexDirection = "row"
        justifyContent = "flex-end"
        alignItems = "center"
        paddingLeft = 8
    }

    val extensionIcon by cssClass {
        boxSizing = "border-box"
        padding = "6px 2px 2px 2px"
        height = 26
        fill = theme.darkColor
        strokeWidth = 2
        cursor = "pointer"
        userSelect = "none"

        on(":first-child") {
            paddingLeft = 7
        }

        on(":last-child") {
            paddingRight = 6
        }
    }


    val extensionIcon16 by cssClass {
        boxSizing = "border-box"
        padding = 5
        height = 26
        width = 26
        fill = theme.darkColor
        strokeWidth = 2
        cursor = "pointer"
        userSelect = "none"
        on(":hover") {
            backgroundColor = theme.gray
            borderRadius = 2
        }
    }

    val activeExtensionIcon by cssClass {
        backgroundColor = theme.selectedColor
        fill = theme.lightestColor
    }

    val path by cssClass {
        position = "relative"
        top = 3
        fontFamily = theme.fontFamily
        fontSize = 12
        fontWeight = 400
        height = 16
        textDecoration = "none"
        color = theme.darkestColor
    }

    val pathItem by cssClass {
        textDecoration = "none"
        color = theme.darkestColor
        cursor = "pointer"
    }

}