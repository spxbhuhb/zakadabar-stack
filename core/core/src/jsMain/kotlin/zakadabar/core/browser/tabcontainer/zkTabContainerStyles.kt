/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.core.browser.tabcontainer

import zakadabar.core.resource.css.*

var zkTabContainerStyles : TabContainerStyleSpec by cssStyleSheet(ZkTabContainerStyles())

open class ZkTabContainerStyles : ZkCssStyleSheet(), TabContainerStyleSpec {

    override var labelTextColor by cssParameter { theme.secondaryPair }
    override var labelBackgroundColor by cssParameter { theme.secondaryColor }
    override var labelHeight by cssParameter { 32 }
    override var activeLabelTextColor by cssParameter { theme.infoPair }
    override var activeLabelBackgroundColor by cssParameter { theme.infoColor }
    override var labelBottomBorder by cssParameter { "none" }
    override var tabBackgroundColor by cssParameter { theme.backgroundColor }

    override val container by cssClass {
        + Position.relative
        + Display.flex
        + FlexDirection.column
    }

    override val labels by cssClass {
        + Display.flex
        + FlexDirection.row
        borderBottom = labelBottomBorder
    }

    override val label by cssClass {
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

    override val activeLabel by cssClass {
        backgroundColor = activeLabelBackgroundColor
        color = activeLabelTextColor
        borderLeft = "1px solid $backgroundColor"
        borderRight = borderLeft
        borderTop = borderLeft

        on(":first-child") {
            borderLeft = "1px solid $backgroundColor"
        }
    }

    override val contentContainer by cssClass {
        + Position.relative
        + Display.flex
        flex = "1 1"
        minHeight = "0"
        height = "calc(100% - ${labelHeight}px)" // TODO think about tab container height management

        if (tabBackgroundColor.isNotEmpty()) { backgroundColor = tabBackgroundColor }
    }

    override val scrolledContent by cssClass {
        + BoxSizing.borderBox
        flexGrow = 1.0
        + OverflowY.auto
    }

    override val border by cssClass {
        border = theme.fixBorder
    }

    override val padding by cssClass {
        padding = theme.spacingStep.px
    }
}