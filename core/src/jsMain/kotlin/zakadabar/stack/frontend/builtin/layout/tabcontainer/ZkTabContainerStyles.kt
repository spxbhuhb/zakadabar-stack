/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout.tabcontainer

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet

object ZkTabContainerStyles : ZkCssStyleSheet<ZkTheme>() {

    val container by cssClass {
        display = "flex"
        flexDirection = "column"
    }

    val labels by cssClass {
        display = "flex"
        flexDirection = "row"
        borderBottom = "1px solid ${theme.tabContainer.labelBottomBorder}"
    }

    val label by cssClass {
        boxSizing = "border-box"
        fontWeight = 400
        fontSize = 14
        minHeight = 32
        minWidth = 100
        backgroundColor = theme.tabContainer.background
        color = theme.tabContainer.foreground
        paddingLeft = 8
        paddingRight = 8
        paddingBottom = 6
        cursor = "pointer"
        display = "flex"
        flexDirection = "column"
        justifyContent = "flex-end"
        whiteSpace = "nowrap"
        borderRight = "1px solid ${ZkColors.Gray.c300}"
        borderTop = "1px solid ${ZkColors.Gray.c300}"

        on(":first-child") {
            borderLeft = "1px solid ${ZkColors.Gray.c300}"
        }
    }

    val activeLabel by cssClass {
        backgroundColor = theme.tabContainer.activeBackground
        color = theme.tabContainer.activeForeground
        borderLeft = "1px solid ${theme.tabContainer.activeBackground}"
        borderRight = borderLeft
        borderTop = borderLeft

        on(":first-child") {
            borderLeft = "1px solid ${theme.tabContainer.activeBackground}"
        }
    }

    val content by cssClass {
        flex = "1 1"
        display = "flex"
        minHeight = "0"
    }

    init {
        attach()
    }
}