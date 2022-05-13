/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.theme.softui.components.suiTheme
import zakadabar.core.browser.titlebar.ZkTitleBarStyles
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders

open class TitleBarStyles : ZkTitleBarStyles() {

    override var appHandleText by cssParameter { suiTheme.colorOnImage }
    override var appTitleBarText by cssParameter { suiTheme.headerTagColor }

    override val appHandleContainer by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        + WhiteSpace.nowrap
        + Cursor.pointer

        height = 100.percent

        paddingRight = 16.px

        large {
            borderRadius = Borders.borderRadius.md
        }

        fontWeight = 500.weight
        fontSize = 120.percent

//        boxShadow = BoxShadows.md
//        color = suiTheme.colorOnImage
//        fill = suiTheme.colorOnImage
//        backgroundImage = suiTheme.backgroundImage

        color = suiTheme.headerTagColor
    }

    override val appHandleButton by cssClass {
        background = "transparent !important"
        fill = "${suiTheme.colorOnImage} !important"
        marginLeft = (theme.spacingStep / 2).px
        marginRight = (theme.spacingStep / 2).px
    }

    override val appTitleBar by cssClass {
        + BoxSizing.borderBox
        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        fontWeight = 500.weight
        width = "calc(100$ - ${theme.spacingStep / 2}px)"
        minHeight = appTitleBarHeight.px
        maxHeight = appTitleBarHeight.px
        borderBottom = appTitleBarBorder
        fontSize = 16.px

        large {
            borderRadius = Borders.borderRadius.md
        }

//        backgroundColor = theme.blockBackgroundColor
//        boxShadow = BoxShadows.md
        color = suiTheme.headerTagColor

//        backgroundImage = suiTheme.backgroundImage
//        backgroundSize = "cover"
//        boxShadow = BoxShadows.md
//        color = suiTheme.colorOnImage

    }

    override val iconButton by cssClass {
        fill = "$appTitleBarText !important"
        backgroundColor = "transparent !important"
        boxShadow = "none !important"
    }
}