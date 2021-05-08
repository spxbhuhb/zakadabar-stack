/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.dock

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkDockStyles by cssStyleSheet(ZkDockStyles())

class ZkDockStyles : ZkCssStyleSheet<ZkTheme>() {

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
        backgroundColor = theme.dock.background
        display = "flex"
        flexDirection = "column"
    }

    val header by cssClass {
        display = "flex"
        flexDirection = "row"
        boxSizing = "border-box"
        minHeight = theme.dock.headerHeight
        height = theme.dock.headerHeight
        alignItems = "center"
        backgroundColor = theme.dock.headerBackground
        overflow = "hidden"
    }

    val headerIcon by cssClass {
        boxSizing = "border-box"
        padding = 6
        height = theme.dock.headerHeight
        width = theme.dock.headerHeight
        backgroundColor = theme.dock.headerIconBackground
        fill = theme.dock.headerIconFill
        marginRight = 8
    }

    val text by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = theme.dock.headerForeground
        fontSize = theme.font.size
        height = 21
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
        height = theme.dock.headerHeight
        backgroundColor = theme.dock.headerIconBackground
        fill = theme.dock.headerIconFill
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

    init {
        attach()
    }
}