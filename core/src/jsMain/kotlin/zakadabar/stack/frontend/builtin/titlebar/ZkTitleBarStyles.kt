/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.titlebar

import zakadabar.stack.frontend.builtin.sidebar.ZkSideBarStyles
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkTitleBarStyles : ZkCssStyleSheet<ZkTheme>() {

    val titleBar by cssClass {
        boxSizing = "border-box"
        fontWeight = 400
        width = "100%"
        minHeight = theme.layout.titleBarHeight
        maxHeight = theme.layout.titleBarHeight
        borderBottom = theme.titleBar.border
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        fontSize = 16
    }

    val sidebarHandle by cssClass {
        fill = theme.sidebar.text
        color = theme.sidebar.text
        minHeight = theme.layout.titleBarHeight
        minWidth = theme.layout.titleBarHeight
        display = "flex"
        alignItems = "center"
        justifyContent = "center"
    }

    val titleContainer by cssClass {
        paddingLeft = theme.layout.paddingStep * 2
        alignItems = "center"
    }

    val title by ZkSideBarStyles.cssClass {
        boxSizing = "border-box"
        fontWeight = 500
        fontSize = "120%"
        borderBottom = theme.titleBar.border
        paddingLeft = 8
        paddingRight = 16
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        minHeight = theme.layout.titleBarHeight
        maxHeight = theme.layout.titleBarHeight
        whiteSpace = "nowrap"
        backgroundColor = theme.sidebar.background
        color = ZkSideBarStyles.theme.sidebar.text
        cursor = "pointer"
    }

    val handleButton by cssClass {
        background = "transparent !important"
        fill = theme.layout.defaultForeground + " !important"
    }

    val contextElementContainer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
    }

    val globalElementContainer by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
    }

    init {
        attach()
    }
}