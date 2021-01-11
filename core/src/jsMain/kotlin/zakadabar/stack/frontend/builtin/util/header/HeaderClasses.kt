/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.util.header

import zakadabar.stack.frontend.application.Application
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.util.CssStyleSheet

open class HeaderClasses(theme: ZkTheme) : CssStyleSheet<HeaderClasses>(theme) {

    companion object {
        var headerClasses = HeaderClasses(Application.theme).attach()
    }

    open val header by cssClass {
        display = "flex"
        flexDirection = "row"
        boxSizing = "border-box"
        minHeight = theme.headerHeight
        height = theme.headerHeight
        alignItems = "center"
        backgroundColor = theme.headerBackground
        overflow = "hidden"
    }

    open val headerIcon by cssClass {
        boxSizing = "border-box"
        padding = 6
        height = theme.headerHeight
        width = theme.headerHeight
        backgroundColor = theme.headerIconBackground
        fill = theme.headerIconFill
        marginRight = 8
    }

    open val headerIcon18 by cssClass {
        boxSizing = "border-box"
        backgroundColor = theme.headerForeground
        padding = "5px 4px 4px 4px"
        height = theme.headerHeight
        width = theme.headerHeight
        backgroundColor = theme.headerIconBackground
        fill = theme.headerIconFill
        marginRight = 8
    }

    open val headerIcon20 by cssClass {
        boxSizing = "border-box"
        backgroundColor = theme.headerForeground
        padding = "4px 3px 3px 3px"
        height = theme.headerHeight
        width = theme.headerHeight
        backgroundColor = theme.headerIconBackground
        fill = theme.headerIconFill
        marginRight = 8
    }

    open val innerIcon by cssClass {
        boxSizing = "border-box"
        padding = "6px 0px 6px 6px"
        height = 26
        fill = theme.headerForeground
        cursor = "pointer"
    }

    open val text by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = theme.headerForeground
        fontSize = theme.fontSize
        height = 21
    }

    open val extensions by cssClass {
        flexGrow = 1
        display = "flex"
        flexDirection = "row"
        justifyContent = "flex-end"
        alignItems = "center"
        paddingLeft = 8
    }

    open val extensionIcon by cssClass {
        boxSizing = "border-box"
        padding = "6px 2px 2px 2px"
        height = theme.headerHeight
        backgroundColor = theme.headerToolBackground
        fill = theme.headerToolFill
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

    open val extensionIcon16 by cssClass {
        boxSizing = "border-box"
        padding = 5
        height = theme.headerHeight
        width = theme.headerHeight
        backgroundColor = theme.headerToolBackground
        fill = theme.headerToolFill
        strokeWidth = 2
        cursor = "pointer"
        userSelect = "none"
        on(":hover") {
            backgroundColor = theme.gray
            borderRadius = 2
        }
    }

    open val activeExtensionIcon by cssClass {
        backgroundColor = theme.selectedColor
        fill = theme.lightestColor
    }

    open val path by cssClass {
        position = "relative"
        top = 3
        fontFamily = theme.fontFamily
        fontSize = 12
        fontWeight = 400
        height = 16
        textDecoration = "none"
        color = theme.headerForeground
    }

    open val pathItem by cssClass {
        textDecoration = "none"
        color = theme.headerForeground
        cursor = "pointer"
    }

}