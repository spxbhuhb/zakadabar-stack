/*
 * Copyright © 2020-2022, Simplexion, Hungary. All rights reserved.
 * Unauthorized use of this code or any part of this code in any form, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 */
package zakadabar.softui.browser.theme.styles

import zakadabar.core.browser.sidebar.ZkSideBarStyles
import zakadabar.core.browser.theme.softui.components.suiTheme
import zakadabar.core.resource.css.*
import zakadabar.softui.browser.theme.base.Borders
import zakadabar.softui.browser.theme.base.BoxShadows

open class SuiSideBarStyles : ZkSideBarStyles() {

    override var backgroundColor by cssParameter { theme.backgroundColor }
    override var textColor by cssParameter { theme.textColor }
    override var itemMinHeight by cssParameter { 36 }
    override var fontSize by cssParameter { "80%" }
    override var iconSize by cssParameter { 18 }
    override var hoverTextColor by cssParameter { theme.hoverTextColor }
    override var sectionBackgroundColor by cssParameter { theme.backgroundColor }
    override var sectionTextColor by cssParameter { suiTheme.textColor }
    override var sectionBorderColor by cssParameter { theme.borderColor }

    override val sidebar by cssClass {
        + Position.relative
        + BoxSizing.borderBox
        + OverflowY.auto

        height = 100.percent
        minWidth = 240.px

        fontSize = this@SuiSideBarStyles.fontSize

        backgroundColor = this@SuiSideBarStyles.backgroundColor
//        backgroundImage = suiTheme.backgroundImage
//        backgroundSize = "cover"

        borderBottomLeftRadius = Borders.borderRadius.md
        borderBottomRightRadius = Borders.borderRadius.md

        boxShadow = BoxShadows.md

        color = this@SuiSideBarStyles.textColor

        small {
            padding = 0.px
            margin = 0.px
        }

        medium {
            padding = 0.px
            margin = 0.px
        }
    }

    override val item by cssClass {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + AlignItems.center

        + Cursor.pointer

        minHeight = itemMinHeight.px
        paddingRight = 8.px
        paddingLeft = 26.px

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = this@SuiSideBarStyles.hoverTextColor
            borderRadius = Borders.borderRadius.md
        }
    }

    override val itemText by cssClass {
        flexGrow = 1.0
        color = "inherit"
        fontSize = 14.px
    }

    override val icon by cssClass {
        + Display.flex
        + JustifyContent.center
        + AlignItems.center
        + Position.relative

        fill = suiTheme.headerTagColor
        width = 28.px + "!important"
        height = 28.px + "!important"
        borderRadius = Borders.borderRadius.sm
        marginRight = (theme.spacingStep / 2).px
        backgroundColor = suiTheme.blockBackgroundColor
        boxShadow = BoxShadows.md
    }

    override fun ZkCssStyleRule.title() {
        + BoxSizing.borderBox

        + Display.flex
        + FlexDirection.row
        + JustifyContent.flexStart
        + AlignItems.center

        + Cursor.pointer

        minHeight = itemMinHeight.px

        hover {
            backgroundColor = theme.hoverBackgroundColor
            color = this@SuiSideBarStyles.hoverTextColor
            borderRadius = Borders.borderRadius.md
        }

        on(" a") {
            flexGrow = 1.0
            color = "inherit"
        }
    }

    override val groupTitle by cssClass {
        title()

        paddingRight = 8.px

        fontSize = 14.px

        fill = this@SuiSideBarStyles.textColor
        color = this@SuiSideBarStyles.textColor

    }

    override val groupArrow by cssClass {
        marginLeft = 6.px
        width = 20.px
    }

    override val groupContent by cssClass {
        paddingLeft = 20.px
    }

    override val sectionTitle by cssClass {
        title()

        marginTop = theme.spacingStep.px

        paddingRight = 8.px
        paddingLeft = 2.px

        + TextTransform.uppercase

        fontSize = 12.px
        fontWeight = 600.weight

        backgroundColor = sectionBackgroundColor
        color = sectionTextColor

        //fill = this@ZkSideBarStyles.textColor.alpha(0.2)

        fill = sectionBorderColor
        borderBottom = Borders.borderColor
    }

    override val sectionCloseIcon by cssClass {
        opacity = 0.opacity

        hover {
            opacity = 1.opacity
        }
    }

    override val sectionContent by cssClass {
        paddingLeft = 0.px
    }

}