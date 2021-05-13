/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.dock

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkDockStyles by cssStyleSheet(ZkDockStyles())

class ZkDockStyles : ZkCssStyleSheet() {

    var dockBackground: String = ZkColors.white
    var headerBackground: String = ZkColors.BlueGray.c600
    var headerForeground: String = ZkColors.black
    var headerIconBackground: String = "transparent"
    var headerIconFill: String = ZkColors.white
    var headerHeight: String = "26px"

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
        backgroundColor = dockBackground
        display = "flex"
        flexDirection = "column"
    }

    val header by cssClass {
        display = "flex"
        flexDirection = "row"
        boxSizing = "border-box"
        minHeight = headerHeight
        height = headerHeight
        alignItems = "center"
        backgroundColor = headerBackground
        overflow = "hidden"
    }

    val headerIcon by cssClass {
        boxSizing = "border-box"
        backgroundColor = headerIconBackground
        fill = headerIconFill
        marginLeft = 8
        marginRight = 8
    }

    val text by cssClass {
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        color = headerForeground
        fontSize = theme.fontSize
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
        backgroundColor = headerIconBackground
        fill = headerIconFill
        strokeWidth = 2
        cursor = "pointer"
        userSelect = "none"
        marginRight = 12
    }
}