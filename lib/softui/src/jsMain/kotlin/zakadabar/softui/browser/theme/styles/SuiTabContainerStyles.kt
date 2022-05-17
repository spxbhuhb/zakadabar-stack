/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.tabcontainer.TabContainerStyleSpec
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows

class SuiTabContainerStyles : ZkCssStyleSheet(), TabContainerStyleSpec {

    override var labelTextColor by cssParameter { theme.textColor }
    override var labelBackgroundColor by cssParameter { theme.blockBackgroundColor }
    override var labelHeight by cssParameter { 32 }
    override var activeLabelTextColor by cssParameter { theme.primaryPair }
    override var activeLabelBackgroundColor by cssParameter { theme.primaryColor }
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
        + JustifyContent.center
        + AlignItems.center
        + WhiteSpace.nowrap

        + Cursor.pointer

        fontWeight = 400.weight
        fontSize = 14.px
        height = labelHeight.px
        color = labelTextColor

        borderRight = theme.border
        borderTop = theme.border

        paddingLeft = 20.px
        paddingRight = 20.px

        marginBottom = 10.px

        borderRadius = Borders.borderRadius.md

        on(":first-child") {
            borderLeft = theme.border
        }
    }

    override val activeLabel by cssClass {
        color = activeLabelTextColor
        backgroundColor = this@SuiTabContainerStyles.activeLabelBackgroundColor
        boxShadow = BoxShadows.md

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

        if (tabBackgroundColor.isNotEmpty()) {
            backgroundColor = tabBackgroundColor
        }
    }

    override val scrolledContent by cssClass {
        + BoxSizing.borderBox
        flexGrow = 1.0
        + OverflowY.auto
    }

    override val border by cssClass {
    }

    override val padding by cssClass {
    }
}