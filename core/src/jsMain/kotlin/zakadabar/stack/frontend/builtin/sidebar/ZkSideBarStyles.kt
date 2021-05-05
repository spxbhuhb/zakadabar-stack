/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkSideBarStyles : ZkCssStyleSheet<ZkTheme>() {

    val sidebar by cssClass {
        boxSizing = "border-box"
        minHeight = "100%"
        backgroundColor = theme.sideBar.background
        color = theme.sideBar.text
        overflowY = "auto"
        minWidth = 220
        paddingTop = 10
        borderRight = theme.sideBar.border
        fontSize = "90%"
    }

    val item by cssClass {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 32
        paddingLeft = 20
        paddingRight = 8
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        on(":hover") {
            backgroundColor = theme.sideBar.hoverBackground
            color = theme.sideBar.hoverText
        }
    }

    val groupTitle by cssClass {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 32
        display = "flex"
        flexDirection = "row"
        justifyContent = "flex-start"
        alignItems = "center"
        paddingRight = 8
        paddingLeft = 14
        on(":hover") {
            backgroundColor = theme.sideBar.hoverBackground
            color = theme.sideBar.hoverText
        }
    }

    val groupContent by cssClass {
        paddingLeft = 20
    }

    init {
        attach()
    }
}