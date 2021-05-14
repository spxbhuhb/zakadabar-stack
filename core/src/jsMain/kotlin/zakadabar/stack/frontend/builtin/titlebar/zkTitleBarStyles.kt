/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkTitleBarStyles by cssStyleSheet(ZkTitleBarStyles())

class ZkTitleBarStyles : ZkCssStyleSheet() {

    var appTitleBarHeight = "44px"
    var appHandleBackground: String? = null
    var appHandleText: String? = null
    var appHandleBorder: String? = null
    var titleBarBackground: String? = null
    var titleBarText: String? = null
    var titleBarBorder: String? = null

    /**
     * Application handle, the button and application name at the top left.
     * [appHandleContainer] is the style for the whole container.
     */
    val appHandleContainer by cssClass {
        boxSizing = "border-box"
        fontWeight = 500
        fontSize = "120%"
        borderBottom = appHandleBorder ?: theme.border
        paddingRight = 16
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        minHeight = appTitleBarHeight
        maxHeight = appTitleBarHeight
        whiteSpace = "nowrap"
        backgroundColor = appHandleBackground ?: theme.backgroundColor
        color = appHandleText ?: theme.textColor
        cursor = "pointer"
    }

    /**
     * Style for the button (a hamburger menu) in the application handle.
     */
    val appHandleButton by cssClass {
        background = "transparent !important"
        fill = "${appHandleText ?: theme.textColor} !important"
        marginLeft = theme.spacingStep / 2
        marginRight = theme.spacingStep / 2
    }

    /**
     * Style for the application title bar. This is the title bar above the content.
     */
    val appTitleBar by cssClass {
        boxSizing = "border-box"
        fontWeight = 400
        width = "100%"
        minHeight = appTitleBarHeight
        maxHeight = appTitleBarHeight
        borderBottom = titleBarBorder ?: theme.border
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        fontSize = 16
        backgroundColor = titleBarBackground ?: theme.backgroundColor
    }

    /**
     * When the side bar is closed a button is shown in the application title
     * bar that opens the sidebar again.
     */
    val sidebarHandle by cssClass {
        background = "transparent !important"
        fill = "${titleBarText ?: theme.textColor} !important"
        minHeight = appTitleBarHeight
        display = "flex"
        alignItems = "center"
        justifyContent = "center"
        marginLeft = theme.spacingStep / 2
    }

    /**
     * Container for the title in the application title bar.
     */
    val titleContainer by cssClass {
        marginLeft = theme.spacingStep
        alignItems = "center"
    }

    /**
     * Container for the context elements in the application title bar.
     */
    val contextElementContainer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
    }

    /**
     * Container for the global elements in the application title bar.
     */
    val globalElementContainer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        paddingRight = 10
    }

    val iconButton by cssClass {
        fill = "${theme.textColor} !important"
        backgroundColor = "transparent !important"
    }
}