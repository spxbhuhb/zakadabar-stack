/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout.tabcontainer

import zakadabar.stack.frontend.resources.css.ZkCssStyleSheet
import zakadabar.stack.frontend.resources.css.cssParameter
import zakadabar.stack.frontend.resources.css.cssStyleSheet

val zkTabContainerStyles by cssStyleSheet(ZkTabContainerStyles())

open class ZkTabContainerStyles : ZkCssStyleSheet() {

    open var labelTextColor by cssParameter { theme.secondaryPair }
    open var labelBackgroundColor by cssParameter { theme.secondaryColor }
    open var labelHeight by cssParameter { 32 }
    open var activeLabelTextColor by cssParameter { theme.infoPair }
    open var activeLabelBackgroundColor by cssParameter { theme.infoColor }
    open var labelBottomBorder by cssParameter { "none" }

    open val container by cssClass {
        position = "relative"
        display = "flex"
        flexDirection = "column"
    }

    open val labels by cssClass {
        display = "flex"
        flexDirection = "row"
        borderBottom = labelBottomBorder
    }

    open val label by cssClass {
        boxSizing = "border-box"
        fontWeight = 400
        fontSize = 14
        height = labelHeight
        minWidth = 100
        backgroundColor = labelBackgroundColor
        color = labelTextColor
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
        backgroundColor = activeLabelBackgroundColor
        color = activeLabelTextColor
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