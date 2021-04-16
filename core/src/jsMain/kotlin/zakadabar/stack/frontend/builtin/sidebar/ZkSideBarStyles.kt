/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.sidebar

import zakadabar.stack.frontend.application.ZkApplication
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkSideBarStyles : ZkCssStyleSheet<ZkSideBarStyles>(ZkApplication.theme) {

    val sidebar by cssClass {
        minHeight = "100%"
        backgroundColor = theme.sidebar.background
        color = theme.sidebar.text
        overflowY = "auto"
        minWidth = 220
    }

    val title by cssClass {
        boxSizing = "border-box"
        fontWeight = 500
        fontSize = "120%"
        borderBottom = "0.5px solid #ccc"
        marginBottom = 4
        paddingLeft = 8
        paddingRight = 16
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        minHeight = "44px"  // linked to PageTitleBar.height
        marginBottom = 8
        whiteSpace = "nowrap"
        backgroundColor = theme.sidebar.background
        color = theme.sidebar.text
    }

    val item by cssClass {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 32
        paddingLeft = 12
        marginRight = 8
        marginLeft = 8
        display = "flex"
        flexDirection = "row"
        alignItems = "center"
        on(":hover") {
            backgroundColor = theme.sidebar.hoverBackground
            color = theme.sidebar.hoverText
            borderRadius = 4
        }
    }

    val groupTitle by cssClass {
        boxSizing = "border-box"
        cursor = "pointer"
        minHeight = 32
        display = "flex"
        flexDirection = "row"
        justifyContent = "space-between"
        alignItems = "center"
        marginLeft = 8
        marginRight = 8
        paddingLeft = 12
        on(":hover") {
            backgroundColor = theme.sidebar.hoverBackground
            color = theme.sidebar.hoverText
            borderRadius = 4
        }
    }

    val groupContent by cssClass {
        paddingLeft = 20
    }

    init {
        attach()
    }
}