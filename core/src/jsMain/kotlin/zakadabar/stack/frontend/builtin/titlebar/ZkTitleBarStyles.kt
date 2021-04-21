/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.builtin.sidebar.ZkSideBarStyles
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkTitleBarStyles : ZkCssStyleSheet<ZkTheme>() {

    /**
     * Application handle, the button and application name at the top left.
     * [appHandleContainer] is the style for the whole container.
     */
    val appHandleContainer by ZkSideBarStyles.cssClass {
        boxSizing = "border-box"
        fontWeight = 500
        fontSize = "120%"
        borderBottom = theme.titleBar.appHandleBorder
        paddingLeft = 8
        paddingRight = 16
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        minHeight = theme.titleBar.height
        maxHeight = theme.titleBar.height
        whiteSpace = "nowrap"
        backgroundColor = theme.titleBar.appHandleBackground
        color = ZkSideBarStyles.theme.sidebar.text
        cursor = "pointer"
    }

    /**
     * Style for the button (a hamburger menu) in the application handle.
     */
    val appHandleButton by cssClass {
        background = "transparent !important"
        fill = theme.titleBar.appHandleForeground + " !important"
        marginLeft = theme.layout.spacingStep / 2
    }

    /**
     * Style for the application title bar. This is the title bar above the content.
     */
    val appTitleBar by cssClass {
        boxSizing = "border-box"
        fontWeight = 400
        width = "100%"
        minHeight = theme.titleBar.height
        maxHeight = theme.titleBar.height
        borderBottom = theme.titleBar.titleBarBorder
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        fontSize = 16
    }

    /**
     * When the side bar is closed a button is shown in the application title
     * bar that opens the sidebar again.
     */
    val sidebarHandle by cssClass {
        background = "transparent !important"
        fill = "${theme.titleBar.titleBarForeground} !important"
        minHeight = theme.titleBar.height
        display = "flex"
        alignItems = "center"
        justifyContent = "center"
    }

    /**
     * Container for the title in the application title bar.
     */
    val titleContainer by cssClass {
        marginLeft = theme.layout.spacingStep
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
    }

    init {
        attach()
    }
}