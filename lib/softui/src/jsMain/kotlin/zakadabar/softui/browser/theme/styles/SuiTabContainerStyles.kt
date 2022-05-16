/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.tabcontainer.ZkTabContainerStyles
import zakadabar.core.browser.theme.softui.components.suiTheme
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows

class SuiTabContainerStyles : ZkTabContainerStyles() {

    override var labelTextColor by cssParameter { theme.textColor }
    override var labelBackgroundColor by cssParameter { theme.blockBackgroundColor }
    override var labelHeight by cssParameter { 32 }
    override var activeLabelTextColor by cssParameter { suiTheme.colorOnImage }
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
        borderLeft = "1px solid $backgroundColor"
        borderRight = borderLeft
        borderTop = borderLeft

        backgroundImage = "linear-gradient(310deg, rgba(20, 23, 39, 0.8), rgba(58, 65, 111, 0.8)), url(\"/curved14.12c9ea54425c4f1bc1d7.jpg\")"
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
}