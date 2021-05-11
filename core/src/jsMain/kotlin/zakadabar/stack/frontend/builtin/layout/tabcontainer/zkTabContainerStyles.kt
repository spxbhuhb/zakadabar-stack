/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout.tabcontainer

import zakadabar.stack.frontend.resources.ZkColors
import zakadabar.stack.frontend.resources.ZkTheme
import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkTabContainerStyles by cssStyleSheet(ZkTabContainerStyles())

class ZkTabContainerStyles : ZkCssStyleSheet<ZkTheme>() {

    val labelTextColor: String = ZkColors.black
    val labelBackgroundColor: String = ZkColors.Gray.c50
    val activeForeground: String = ZkColors.white
    val activeBackground: String = ZkColors.LightBlue.a400
    val labelBottomBorder: String = ZkColors.LightBlue.a400

    val container by cssClass {
        display = "flex"
        flexDirection = "column"
    }

    val labels by cssClass {
        display = "flex"
        flexDirection = "row"
        borderBottom = "1px solid $labelBottomBorder"
    }

    val label by cssClass {
        boxSizing = "border-box"
        fontWeight = 400
        fontSize = 14
        minHeight = 32
        minWidth = 100
        backgroundColor = labelBackgroundColor
        color = labelTextColor
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
        backgroundColor = activeBackground
        color = activeForeground
        borderLeft = "1px solid $activeBackground"
        borderRight = borderLeft
        borderTop = borderLeft

        on(":first-child") {
            borderLeft = "1px solid $activeBackground"
        }
    }

    val content by cssClass {
        flex = "1 1"
        display = "flex"
        minHeight = "0"
    }
}