/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.stack.frontend.builtin.layout.tabcontainer

import zakadabar.stack.frontend.resources.css.*

val zkTabContainerStyles by cssStyleSheet(ZkTabContainerStyles())

open class ZkTabContainerStyles : ZkCssStyleSheet() {

    open var labelTextColor by cssParameter { theme.secondaryPair }
    open var labelBackgroundColor by cssParameter { theme.secondaryColor }
    open var labelHeight by cssParameter { 32 }
    open var activeLabelTextColor by cssParameter { theme.infoPair }
    open var activeLabelBackgroundColor by cssParameter { theme.infoColor }
    open var labelBottomBorder by cssParameter { "none" }

    open val container by cssClass {
        + Position.relative
        + Display.flex
        + FlexDirection.column
    }

    open val labels by cssClass {
        + Display.flex
        + FlexDirection.row
        borderBottom = labelBottomBorder
    }

    open val label by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.column
        + JustifyContent.flexEnd
        + WhiteSpace.nowrap

        + Cursor.pointer

        fontWeight = 400.weight
        fontSize = 14.px
        height = labelHeight.px
        minWidth = 100.px
        backgroundColor = labelBackgroundColor
        color = labelTextColor
        paddingLeft = theme.spacingStep.px
        paddingRight = theme.spacingStep.px
        paddingBottom = 6.px

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
        + Position.relative
        + Display.flex
        flex = "1 1"
        minHeight = "0"
        height = "calc(100% - ${labelHeight}px)" // TODO think about tab container height management
    }

    open val scrolledContent by cssClass {
        + BoxSizing.borderBox
        flexGrow = 1.0
        + OverflowY.auto
    }
}