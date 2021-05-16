/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout.tabcontainer

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkTabContainerStyles by cssStyleSheet(ZkTabContainerStyles())

open class ZkTabContainerStyles : ZkCssStyleSheet() {

    open var labelTextColor: String? = null
    open var labelBackgroundColor: String? = null
    open var labelHeight = 32
    open var activeForeground: String? = null
    open var activeBackground: String? = null
    open var labelBottomBorder: String? = null

    open val container by cssClass {
        position = "relative"
        display = "flex"
        flexDirection = "column"
    }

    open val labels by cssClass {
        display = "flex"
        flexDirection = "row"
        borderBottom = labelBottomBorder ?: "none"
    }

    open val label by cssClass {
        boxSizing = "border-box"
        fontWeight = 400
        fontSize = 14
        height = labelHeight
        minWidth = 100
        backgroundColor = labelBackgroundColor ?: theme.secondaryColor
        color = labelTextColor ?: theme.secondaryPair
        paddingLeft = theme.spacingStep
        paddingRight = theme.spacingStep
        paddingBottom = 6
        cursor = "pointer"
        display = "flex"
        flexDirection = "column"
        justifyContent = "flex-end"
        whiteSpace = "nowrap"
        borderRight = theme.border
        borderTop = theme.border

        on(":first-child") {
            borderLeft = theme.border
        }
    }

    open val activeLabel by cssClass {
        backgroundColor = activeBackground ?: theme.infoColor
        color = activeForeground ?: theme.infoPair
        borderLeft = "1px solid $backgroundColor"
        borderRight = borderLeft
        borderTop = borderLeft

        on(":first-child") {
            borderLeft = "1px solid $backgroundColor"
        }
    }

    open val contentContainer by cssClass {
        position = "relative"
        flex = "1 1"
        display = "flex"
        minHeight = "0"
        height = "calc(100% - ${labelHeight}px)" // TODO think about tab container height management
    }

    open val scrolledContent by cssClass {
        boxSizing = "border-box"
        flexGrow = 1
        overflowY = "auto"
    }
}